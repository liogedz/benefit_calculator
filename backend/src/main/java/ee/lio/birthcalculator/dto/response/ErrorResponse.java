package ee.lio.birthcalculator.dto.response;

import java.time.LocalDateTime;

public record ErrorResponse(String error, int status,
                            LocalDateTime timestamp) {
}
