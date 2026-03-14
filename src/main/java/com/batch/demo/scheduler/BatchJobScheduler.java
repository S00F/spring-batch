package com.batch.demo.scheduler;

import com.batch.demo.config.BatchConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * Scheduler pour exécuter les jobs Spring Batch automatiquement.
 * 
 * Pour activer le scheduling, ajouter @EnableScheduling sur la classe principale
 * ou sur une classe de configuration.
 */
@Configuration
@EnableScheduling
public class BatchJobScheduler {

    private static final Logger log = LoggerFactory.getLogger(BatchJobScheduler.class);

    private final JobLauncher jobLauncher;
    private final Job userJob;

    @Value("${batch.jobs.schedule.enabled:false}")
    private boolean scheduleEnabled;

    public BatchJobScheduler(JobLauncher jobLauncher, Job userJob) {
        this.jobLauncher = jobLauncher;
        this.userJob = userJob;
    }


    @Scheduled(cron = "${batch.jobs.schedule.cron:0 0 2 * * ?}")
    public void runDailyJob() throws Exception {
        if (!scheduleEnabled) {
            log.info("Scheduled jobs are disabled. Set batch.jobs.schedule.enabled=true to enable.");
            return;
        }

        log.info("========================================");
        log.info("Starting scheduled daily batch job");
        log.info("========================================");

        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .addString("scheduled", "true")
                    .toJobParameters();

            jobLauncher.run(userJob, params);
            log.info("Scheduled job completed successfully");
        } catch (Exception e) {
            log.error("Scheduled job failed", e);
            throw e;
        }
    }

    /**
     * Exécuter le job toutes les heures.
     * Décommenter pour activer.
     */
    // @Scheduled(fixedRate = 3600000) // toutes les heures en millisecondes
    public void runHourlyJob() throws Exception {
        if (!scheduleEnabled) {
            return;
        }

        log.info("Starting hourly batch job");

        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("scheduled", "hourly")
                .toJobParameters();

        jobLauncher.run(userJob, params);
    }

    /**
     * Exécuter le job toutes les 30 minutes.
     * Décommenter pour activer.
     */
    // @Scheduled(fixedDelay = 1800000) // toutes les 30 minutes
    public void runEvery30Minutes() throws Exception {
        if (!scheduleEnabled) {
            return;
        }

        log.info("Starting batch job every 30 minutes");

        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .addString("scheduled", "every30min")
                .toJobParameters();

        jobLauncher.run(userJob, params);
    }
}
