package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.exceptions.SessionNotFoundException;
import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.repository.BenefitSessionRepository;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.transaction.annotation.Transactional;
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


    @Override
    @Transactional
    public String createSession() {

        BenefitSession session = new BenefitSession();
        session.setSessionId(UUID.randomUUID().toString());
        repository.save(session);
        return session.getSessionId();
    }

    @Override
    @Transactional
    public void saveSessionData(String sessionId,
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
        repository.save(session);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<BenefitSession> getSession(String sessionId) {
        return repository.findBySessionId(sessionId);
    }

    @Override
    @Transactional(readOnly = true)
    public BenefitRequest getSessionData(String sessionId) {

        BenefitSession session = repository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        return new BenefitRequest(
                session.getGrossSalary(),
                session.getBirthDate()
        );
    }
}
