package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.dto.response.BenefitMonth;
import ee.lio.birthcalculator.dto.response.CalculationResult;
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
    public CalculationResult calculate(Double grossSalary,
                                       LocalDate birthDate) {
        boolean isCapped = grossSalary > SALARY_CAP;
        double effectiveSalary = Math.min(grossSalary,
                SALARY_CAP);

        double dailyRate = effectiveSalary / 30.0;
        List<BenefitMonth> monthlyBenefit = new ArrayList<>();
        YearMonth startMonth = YearMonth.from(birthDate);

        for (int i = 0; i < 12; i++) {

            YearMonth currentMonth = startMonth.plusMonths(i);
            LocalDate start = (i == 0)
                    ? birthDate
                    : currentMonth.atDay(1);

            LocalDate end = currentMonth.atEndOfMonth();

            int days = (int) ChronoUnit.DAYS.between(start,
                    end) + 1;

            double payment = round(dailyRate * days);

            monthlyBenefit.add(new BenefitMonth(
                    formatMonth(currentMonth),
                    start,
                    end,
                    days,
                    payment
            ));
        }

        return new CalculationResult(isCapped,
                SALARY_CAP,
                monthlyBenefit);
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }

    private String formatMonth(YearMonth ym) {
        return ym.getMonth().getDisplayName(TextStyle.FULL,
                Locale.ENGLISH)
                + " " + ym.getYear();
    }
}
