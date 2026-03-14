package com.batch.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionListener implements JobExecutionListener {

    private static final Logger logger =
            LoggerFactory.getLogger(JobCompletionListener.class);

    @Override
    public void beforeJob(JobExecution jobExecution) {
        jobExecution.getExecutionContext()
                .putLong("startTime", System.currentTimeMillis());
        logger.info("JOB STARTED: {}", jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {


        long start =
                jobExecution.getExecutionContext().getLong("startTime");

        long duration = System.currentTimeMillis() - start;


        logger.info("JOB STATUS: {}", jobExecution.getStatus());

        if (jobExecution.getStatus().isUnsuccessful()) {
            jobExecution.getAllFailureExceptions()
                    .forEach(e -> logger.error("Failure: {}", e.getMessage()));
            logger.error("Job failed!");
        } else {
            logger.info("User import completed successfully!");
        }

        logger.info("Job finished in {} ms", duration);

    }
}