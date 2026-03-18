package ee.lio.birthcalculator.repository;

import ee.lio.birthcalculator.model.BenefitSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class BenefitSessionRepositoryTest {

    @Autowired
    private BenefitSessionRepository repository;

    @BeforeEach
    void setUp() {
        BenefitSession session = new BenefitSession();
        session.setSessionId("test-session-123");
        session.setGrossSalary(12345.0);
        session.setBirthDate(LocalDate.of(1990,
                1,
                15));
        BenefitSession saved = repository.save(session);
    }

    @Test
    void findBySessionId_shouldReturnSession() {
        Optional<BenefitSession> result = repository.findBySessionId("test-session-123");

        assertThat(result).isPresent();
        assertThat(result.get().getGrossSalary()).isEqualTo(12345.0);
        assertThat(result.get().getBirthDate()).isEqualTo(LocalDate.of(1990,
                1,
                15));
    }

    @Test
    void findBySessionId_shouldReturnEmpty_whenNotFound() {
        Optional<BenefitSession> result = repository.findBySessionId("nonexistent");
        assertThat(result).isEmpty();
    }

    @Test
    void deleteOlderThan_shouldRemoveOldSessions() {

        LocalDateTime futureThreshold = LocalDateTime.now().plusMinutes(1);
        repository.deleteOlderThan(futureThreshold);
        assertThat(repository.findBySessionId("test-session-123")).isEmpty();
    }

    @Test
    void deleteOlderThan_shouldKeepRecentSessions() {
        LocalDateTime pastThreshold = LocalDateTime.now().minusMinutes(1);

        repository.deleteOlderThan(pastThreshold);

        assertThat(repository.findBySessionId("test-session-123")).isPresent();
    }
}
