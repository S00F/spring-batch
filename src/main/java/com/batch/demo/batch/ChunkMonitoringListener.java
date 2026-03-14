package com.batch.demo.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

import org.springframework.stereotype.Component;

@Component
public class ChunkMonitoringListener implements ChunkListener {

    private static final Logger log = LoggerFactory.getLogger(ChunkMonitoringListener.class);

    @Override
    public void beforeChunk(ChunkContext context) {
        log.info("Starting chunk at position: {}", 
            context.getStepContext().getStepExecution().getWriteCount());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        log.info("Completed chunk. Items written in this chunk: {}", 
            context.getStepContext().getStepExecution().getWriteCount());
    }

    @Override
    public void afterChunkError(ChunkContext context) {
        log.error("Error occurred during chunk processing at position: {}", 
            context.getStepContext().getStepExecution().getWriteCount());
    }
}
