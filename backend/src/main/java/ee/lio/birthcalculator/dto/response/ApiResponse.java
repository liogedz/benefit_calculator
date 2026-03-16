package ee.lio.birthcalculator.dto.response;

import jakarta.validation.constraints.NotNull;

public record ApiResponse(
        @NotNull
        String message,
        Object data
) {
}