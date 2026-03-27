package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.response.CalculationResult;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CalculatorService {
    CalculationResult calculate(BigDecimal grossSalary,
                                LocalDate birthDate);
}
