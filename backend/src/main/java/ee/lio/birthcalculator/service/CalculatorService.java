package ee.lio.birthcalculator.service;

import ee.lio.birthcalculator.dto.response.CalculationResult;

import java.time.LocalDate;

public interface CalculatorService {
    CalculationResult calculate(Double grossSalary,
                                LocalDate birthDate);
}
