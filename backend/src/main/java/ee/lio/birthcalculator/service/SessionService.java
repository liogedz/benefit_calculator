package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.model.BenefitSession;

import java.time.LocalDate;
import java.util.Optional;

public interface SessionService {
    String createSession();

    void saveSessionData(String sessionId,
                         Double salary,
                         LocalDate dob);

    Optional<BenefitSession> getSession(String sessionId);

    BenefitRequest getSessionData(String sessionId);
}
