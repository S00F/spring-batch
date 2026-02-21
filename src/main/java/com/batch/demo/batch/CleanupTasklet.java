package com.batch.demo.batch;

import com.batch.demo.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class CleanupTasklet implements Tasklet {

    private static final Logger logger =
            LoggerFactory.getLogger(CleanupTasklet.class);

    private final UserRepository repository;

    public CleanupTasklet(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public RepeatStatus execute(
            StepContribution contribution,
            ChunkContext chunkContext) {

        logger.info("Cleaning users table...");
        repository.deleteAll();

        return RepeatStatus.FINISHED;
    }
}