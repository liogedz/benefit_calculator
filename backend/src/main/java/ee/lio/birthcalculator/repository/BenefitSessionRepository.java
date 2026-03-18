package ee.lio.birthcalculator.repository;

import ee.lio.birthcalculator.model.BenefitSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BenefitSessionRepository extends JpaRepository<BenefitSession, Long> {
    Optional<BenefitSession> findBySessionId(String sessionId);

    @Modifying
    @Query("DELETE FROM BenefitSession s WHERE s.lastAccessed < :threshold")
    void deleteOlderThan(LocalDateTime threshold);
}
