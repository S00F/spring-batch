package com.batch.demo.config;

import com.batch.demo.batch.*;
import com.batch.demo.model.User;
import com.batch.demo.model.UserDto;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JpaItemWriter;
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
    private final EntityManagerFactory entityManagerFactory;
    private final CleanupTasklet cleanupTasklet;
    private final SummaryTasklet summaryTasklet;
    private final JobCompletionListener jobExecutionListener;



    public BatchConfig(JobRepository jobRepository,
                       PlatformTransactionManager transactionManager,
                       UserReader userReader,
                       UserItemProcessor userItemProcessor,
                       UserDtoWriter userDtoWriter, EntityManagerFactory entityManagerFactory, CleanupTasklet cleanupTasklet, SummaryTasklet summaryTasklet, JobCompletionListener jobExecutionListener) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.userReader = userReader;
        this.userItemProcessor = userItemProcessor;
        this.userDtoWriter = userDtoWriter;
        this.entityManagerFactory = entityManagerFactory;
        this.cleanupTasklet = cleanupTasklet;
        this.summaryTasklet = summaryTasklet;
        this.jobExecutionListener = jobExecutionListener;
    }

    @Bean
    public JpaItemWriter<User> userWriter() {
        JpaItemWriter<User> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return writer;
    }

    @Bean
    public Step cleanupStep() {
        return new StepBuilder("cleanupStep", jobRepository)
                .tasklet(cleanupTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step summaryStep() {
        return new StepBuilder("summaryStep", jobRepository)
                .tasklet(summaryTasklet, transactionManager)
                .build();
    }

    @Bean
    public Step userMappingStep() {
        return new StepBuilder("userMappingStep", jobRepository)
                .<UserDto, User>chunk(3, transactionManager)
                .reader(userReader)
                .processor(userItemProcessor)
                .writer(userDtoWriter)
                .build();
    }

    @Bean
    public Job userJob() {

        return new JobBuilder("userJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(jobExecutionListener)
                .start(cleanupStep())
                .next(userMappingStep())
                    .on(BatchStatus.FAILED.name()).end()
                    .on(BatchStatus.COMPLETED.name()).to(summaryStep()).end()
                .build();
    }



}