package ee.lio.birthcalculator.service.impl;

import ee.lio.birthcalculator.dto.response.BenefitMonth;
import ee.lio.birthcalculator.dto.response.CalculationResult;
import ee.lio.birthcalculator.service.CalculatorService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class CalculatorServiceImpl implements CalculatorService {

    private static final BigDecimal SALARY_CAP = new BigDecimal("4000.00");
    private static final BigDecimal DAILY_DIVISOR = new BigDecimal("30");
    private static final int PERIOD = 12;

    @Override
    public CalculationResult calculate(BigDecimal grossSalary,
                                       LocalDate birthDate) {
        boolean isCapped = grossSalary.compareTo(SALARY_CAP) > 0;
        BigDecimal effectiveSalary = grossSalary.min(SALARY_CAP);
        BigDecimal dailyRate = effectiveSalary.divide(DAILY_DIVISOR,
                10,
                RoundingMode.HALF_UP);

        List<BenefitMonth> monthlyBenefit = new ArrayList<>();
        YearMonth startMonth = YearMonth.from(birthDate);

        for (int i = 0; i < PERIOD; i++) {
            YearMonth currentMonth = startMonth.plusMonths(i);
            LocalDate start = (i == 0) ? birthDate : currentMonth.atDay(1);
            LocalDate end = currentMonth.atEndOfMonth();

            int days = (int) ChronoUnit.DAYS.between(start,
                    end) + 1;

            BigDecimal payment = dailyRate
                    .multiply(BigDecimal.valueOf(days))
                    .min(SALARY_CAP)
                    .setScale(2,
                            RoundingMode.HALF_UP);

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

    private String formatMonth(YearMonth ym) {
        return ym.getMonth().getDisplayName(TextStyle.FULL,
                Locale.ENGLISH)
                + " " + ym.getYear();
    }
}
