package ee.lio.birthcalculator.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BenefitRequest(
        @NotNull
        @Positive
        Double salary,

        @NotNull
        LocalDate dob
) {
}
