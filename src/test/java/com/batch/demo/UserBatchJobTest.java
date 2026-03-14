package com.batch.demo;

import com.batch.demo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBatchTest
@SpringBootTest
class UserBatchJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private Job userJob;

    @BeforeEach
    void setUp() {
        jobLauncherTestUtils.setJob(userJob);
        jobRepositoryTestUtils.removeJobExecutions();
        userRepository.deleteAll();
    }

    @Test
    void jobShouldCompleteSuccessfully() throws Exception {
        JobExecution execution = jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        assertEquals(BatchStatus.COMPLETED, execution.getStatus());
    }

    @Test
    void jobShouldImportAllUsersFromCsv() throws Exception {
        jobLauncherTestUtils.launchJob(
                new JobParametersBuilder()
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters()
        );

        assertEquals(3, userRepository.count());
    }

    @Test
    void cleanupStepShouldClearExistingData() throws Exception {
        // Pre-populate to verify cleanup step removes existing records
        jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addLong("time", 1L).toJobParameters()
        );
        assertEquals(3, userRepository.count());

        // Re-run: cleanup step should delete, then re-import
        jobLauncherTestUtils.launchJob(
                new JobParametersBuilder().addLong("time", 2L).toJobParameters()
        );
        assertEquals(3, userRepository.count());
    }
}