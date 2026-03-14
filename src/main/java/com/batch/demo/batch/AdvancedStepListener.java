package com.batch.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;

import org.springframework.stereotype.Component;

@Component
public class AdvancedStepListener implements StepExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(AdvancedStepListener.class);

    @Override
    public void beforeStep(StepExecution stepExecution) {
        log.info("========================================");
        log.info("Starting Step: {}", stepExecution.getStepName());
        log.info("Job Parameters: {}", stepExecution.getJobParameters());
        log.info("========================================");
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        log.info("========================================");
        log.info("Step Completed: {}", stepExecution.getStepName());
        log.info("Read Count: {}", stepExecution.getReadCount());
        log.info("Write Count: {}", stepExecution.getWriteCount());
        log.info("Skip Count: {}", stepExecution.getSkipCount());
        log.info("Commit Count: {}", stepExecution.getCommitCount());
        log.info("Rollback Count: {}", stepExecution.getRollbackCount());
        log.info("========================================");

        return stepExecution.getStatus() == BatchStatus.FAILED 
            ? ExitStatus.FAILED 
            : ExitStatus.COMPLETED;
    }
}
