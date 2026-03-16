package ee.lio.birthcalculator.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface CalculatorService {
    List<Map<String, Object>> calculate(Double grossSalary,
                                        LocalDate birthDate);
}
