package ee.lio.birthcalculator.controller;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.dto.response.ApiResponse;
import ee.lio.birthcalculator.dto.response.BenefitMonth;
import ee.lio.birthcalculator.exceptions.SessionNotFoundException;
import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.service.CalculatorService;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class BenefitController {

    private final CalculatorService calculatorService;
    private final SessionService sessionService;

    public BenefitController(CalculatorService calculatorService,
                             SessionService sessionService) {
        this.calculatorService = calculatorService;
        this.sessionService = sessionService;
    }

    @PostMapping
    public Map<String, String> createSession() {
        String sessionId = sessionService.createSession();
        return Map.of("sessionId",
                sessionId);
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<?> saveSession(@PathVariable String sessionId,
                                         @RequestBody BenefitRequest request) {
        Double salary = request.salary();
        LocalDate dob = request.dob();
        sessionService.saveSessionData(sessionId,
                salary,
                dob);
        return ResponseEntity.ok(Map.of("message",
                "Saved"));
    }

    @GetMapping("/{sessionId}/calculator")
    public ResponseEntity<ApiResponse> calculateSession(@PathVariable String sessionId) {
        BenefitSession session = sessionService.getSession(sessionId)
                .orElseThrow(() -> new SessionNotFoundException(sessionId));

        List<BenefitMonth> data = calculatorService.calculate(
                session.getGrossSalary(),
                session.getBirthDate());

        return ResponseEntity.ok(new ApiResponse("Yearly breakdown calculated",
                data));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<BenefitRequest> getSession(
            @PathVariable String sessionId) {

        return ResponseEntity.ok(
                sessionService.getSessionData(sessionId)
        );
    }
}
