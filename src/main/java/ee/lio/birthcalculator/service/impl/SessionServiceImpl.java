package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.repository.BenefitSessionRepository;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {

    private final BenefitSessionRepository repository;

    public SessionServiceImpl(BenefitSessionRepository repository) {
        this.repository = repository;
    }

    public String createSession() {

        BenefitSession session = new BenefitSession();
        session.setSessionId(UUID.randomUUID().toString());
        repository.save(session);
        return session.getSessionId();
    }

    public BenefitSession saveSessionData(String sessionId,
                                          Double salary,
                                          LocalDate dob) {
        BenefitSession session = repository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    BenefitSession s = new BenefitSession();
                    s.setSessionId(sessionId);
                    return s;
                });
        session.setGrossSalary(salary);
        session.setBirthDate(dob);
        return repository.save(session);
    }

    public Optional<BenefitSession> findBySessionId(String sessionId) {
        return repository.findBySessionId(sessionId);
    }
}
