package ee.lio.birthcalculator.job;

import ee.lio.birthcalculator.repository.BenefitSessionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
public class SessionCleanupJob {
    private static final Logger log = LoggerFactory.getLogger(SessionCleanupJob.class);
    private final BenefitSessionRepository repository;

    public SessionCleanupJob(BenefitSessionRepository repository) {
        this.repository = repository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void runOnStartup() {
        cleanupOldSessions();
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    public void cleanupOldSessions() {

        LocalDateTime threshold = LocalDateTime.now().minusDays(7);
        repository.deleteOlderThan(threshold);
        log.info("Old sessions cleaned up");
    }
}
