package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.model.BenefitSession;

import java.time.LocalDate;

public interface SessionService {
    String createSession();

    void saveSessionData(String sessionId,
                         Double salary,
                         LocalDate dob);

    BenefitSession getSession(String sessionId);

    BenefitRequest getSessionData(String sessionId);
}
