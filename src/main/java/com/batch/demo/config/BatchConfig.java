package com.batch.demo.config;

import com.batch.demo.batch.*;
import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final UserReader userReader;
    private final UserItemProcessor userItemProcessor;
    private final UserDtoWriter userDtoWriter;
    private final CleanupTasklet cleanupTasklet;
    private final JobCompletionListener jobCompletionListener;
    private final AdvancedStepListener advancedStepListener;
    private final ChunkMonitoringListener chunkMonitoringListener;

    @Value("${batch.chunk.size:10}")
    private int chunkSize;

    @Value("${batch.skip.limit:10}")
    private int skipLimit;

    @Value("${batch.retry.max-attempts:3}")
    private int retryMaxAttempts;

    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       UserReader userReader,
                       UserItemProcessor userItemProcessor,
                       UserDtoWriter userDtoWriter,
                       CleanupTasklet cleanupTasklet,
                       JobCompletionListener jobCompletionListener,
                       AdvancedStepListener advancedStepListener,
                       ChunkMonitoringListener chunkMonitoringListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.userReader = userReader;
        this.userItemProcessor = userItemProcessor;
        this.userDtoWriter = userDtoWriter;
        this.cleanupTasklet = cleanupTasklet;
        this.jobCompletionListener = jobCompletionListener;
        this.advancedStepListener = advancedStepListener;
        this.chunkMonitoringListener = chunkMonitoringListener;
    }

    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet(cleanupTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step userMappingStep() {
        return new StepBuilder("userMappingStep", jobRepository)
                .<UserDto, User>chunk(chunkSize, transactionManager)
                .reader(userReader)
                .processor(userItemProcessor)
                .writer(userDtoWriter)
                .faultTolerant()
                .skip(Exception.class).skipLimit(skipLimit)
                .retry(Exception.class).retryLimit(retryMaxAttempts)
                .listener(advancedStepListener)
                .listener(chunkMonitoringListener)
                .build();
    }

    @Bean
    public Job userJob() {
        return new JobBuilder("userJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobCompletionListener)
                .start(cleanupStep())
                .next(userMappingStep())
                .build();
    }
}