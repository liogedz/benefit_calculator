package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.exceptions.SessionNotFoundException;
import ee.lio.birthcalculator.exceptions.ValidationException;
import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.repository.BenefitSessionRepository;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SessionServiceImpl implements SessionService {
    private static final int MIN_SALARY = 100;
    private static final int MAX_AGE = 3;
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
        validate(salary,
                dob);

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
    @Transactional
    public BenefitSession getSession(String sessionId) {
        BenefitSession session = repository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
        touch(session);
        return session;
    }

    @Override
    @Transactional
    public BenefitRequest getSessionData(String sessionId) {

        BenefitSession session = repository.findBySessionId(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));
        touch(session);

        return new BenefitRequest(
                session.getGrossSalary(),
                session.getBirthDate()
        );
    }

    private void touch(BenefitSession session) {
        session.setLastAccessed(LocalDateTime.now());
    }

    private void validate(Double salary,
                          LocalDate dob) {

        if (salary == null || salary < MIN_SALARY) {
            throw new ValidationException("Salary must be at least 100");
        }

        if (dob.isAfter(LocalDate.now())) {
            throw new ValidationException("Date of birth cannot be in the future");
        }

        if (dob.isBefore(LocalDate.now().minusYears(MAX_AGE))) {
            throw new ValidationException("Child is older than 3 years");
        }
    }
}
