package ee.lio.birthcalculator.dto.response;

import jakarta.validation.constraints.NotNull;

public record ApiResponse<T>(
        @NotNull
        String message,
        T data
) {
}