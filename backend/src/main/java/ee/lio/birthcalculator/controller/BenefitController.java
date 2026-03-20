package ee.lio.birthcalculator.controller;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.dto.response.ApiResponse;
import ee.lio.birthcalculator.dto.response.CalculationResult;
import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.service.CalculatorService;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
    public ResponseEntity<ApiResponse<String>> createSession() {
        String sessionId = sessionService.createSession();
        return ResponseEntity.ok(new ApiResponse<>("Session created",
                sessionId));
    }

    @PostMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<Void>> saveSession(@PathVariable String sessionId,
                                                         @RequestBody BenefitRequest request) {
        Double salary = request.salary();
        LocalDate dob = request.dob();
        sessionService.saveSessionData(sessionId,
                salary,
                dob);
        return ResponseEntity.ok(new ApiResponse<>("Session saved",
                null
        ));
    }

    @GetMapping("/{sessionId}/calculator")
    public ResponseEntity<ApiResponse<CalculationResult>> calculateSession(@PathVariable String sessionId) {
        BenefitSession session = sessionService.getSession(sessionId);
        CalculationResult data = calculatorService.calculate(
                session.getGrossSalary(),
                session.getBirthDate());

        return ResponseEntity.ok(new ApiResponse<>("Yearly breakdown calculated",
                data));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<ApiResponse<BenefitRequest>> getSession(
            @PathVariable String sessionId) {
        return ResponseEntity.ok(new ApiResponse<>("Your session",
                sessionService.getSessionData(sessionId)
        ));
    }
}
