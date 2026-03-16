package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.dto.response.BenefitMonth;
import ee.lio.birthcalculator.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static java.lang.Math.round;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final double SALARY_CAP = 4000.0;

    @Override
    public List<BenefitMonth> calculate(Double grossSalary,
                                        LocalDate birthDate) {

        double effectiveSalary = Math.min(grossSalary,
                SALARY_CAP);
        double dailyRate = effectiveSalary / 30.0;

        List<BenefitMonth> result = new ArrayList<>();

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

            result.add(new BenefitMonth(
                    formatMonth(currentMonth),
                    start,
                    end,
                    days,
                    payment
            ));
        }

        return result;
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
