package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.model.BenefitSession;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface SessionService {
    String createSession();

    void saveSessionData(String sessionId,
                         BigDecimal salary,
                         LocalDate dob);

    BenefitSession getSession(String sessionId);

    BenefitRequest getSessionData(String sessionId);
}
