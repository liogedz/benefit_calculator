package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.response.BenefitMonth;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CalculatorService {
    List<BenefitMonth> calculate(Double grossSalary,
                                 LocalDate birthDate);
}
