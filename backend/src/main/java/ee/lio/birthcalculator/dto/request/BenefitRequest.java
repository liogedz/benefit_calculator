package ee.lio.birthcalculator.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record BenefitRequest(
        @NotNull
        @Positive
        Double salary,

        @NotNull
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate dob
) {
}
