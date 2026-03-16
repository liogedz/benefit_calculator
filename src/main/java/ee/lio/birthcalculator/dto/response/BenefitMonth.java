package ee.lio.birthcalculator.dto.response;

import java.time.LocalDate;

public record BenefitMonth(
        String month,
        LocalDate start,
        LocalDate end,
        int days,
        double payment
) {
}
