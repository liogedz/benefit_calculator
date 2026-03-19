package ee.lio.birthcalculator.advice;

import ee.lio.birthcalculator.dto.response.ErrorResponse;
import ee.lio.birthcalculator.exceptions.SessionNotFoundException;
import ee.lio.birthcalculator.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleSessionNotFound(SessionNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(ex.getMessage(),
                        404,
                        LocalDateTime.now()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidation(ValidationException ex) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse(ex.getMessage(),
                        400,
                        LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidFormat(HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest()
                .body(new ErrorResponse(
                        ex.getMessage().contains("LocalDate") ? "Invalid date format. Use yyyy-MM-dd"
                                : "Invalid request format. Expected date format: yyyy-MM-dd",
                        400,
                        LocalDateTime.now()
                ));
    }
}