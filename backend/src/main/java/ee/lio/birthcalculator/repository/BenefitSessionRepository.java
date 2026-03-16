package ee.lio.birthcalculator.repository;

import ee.lio.birthcalculator.model.BenefitSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BenefitSessionRepository extends JpaRepository<BenefitSession, Long> {
    Optional<BenefitSession> findBySessionId(String sessionId);

}
