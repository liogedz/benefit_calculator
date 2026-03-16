package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CalculatorServiceImpl implements CalculatorService {
    private static final double SALARY_CAP = 4000.0;

    @Override
    public List<Map<String, Object>> calculate(Double grossSalary,
                                               LocalDate birthDate) {
        double effectiveSalary = Math.min(grossSalary,
                SALARY_CAP);
        double dailyRate = effectiveSalary / 30.0;

        List<Map<String, Object>> result = new ArrayList<>();

        for (int i = 0; i < 12; i++) {
            YearMonth yearMonth = YearMonth.from(birthDate).plusMonths(i);

            LocalDate periodStart = (i == 0) ? birthDate : yearMonth.atDay(1);
            LocalDate periodEnd = yearMonth.atEndOfMonth();

            int days = (int) ChronoUnit.DAYS.between(periodStart,
                    periodEnd) + 1;
            double payment = Math.round(dailyRate * days * 100.0) / 100.0;

            Map<String, Object> month = new LinkedHashMap<>();
            month.put("month",
                    yearMonth.getMonth().getDisplayName(TextStyle.FULL,
                            Locale.ENGLISH) + " " + yearMonth.getYear());
            month.put("periodStart",
                    periodStart.toString());
            month.put("periodEnd",
                    periodEnd.toString());
            month.put("days",
                    days);
            month.put("payment",
                    payment);

            result.add(month);
        }

        return result;
    }

}
