package ee.lio.birthcalculator.controller;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.dto.response.ApiResponse;
import ee.lio.birthcalculator.service.CalculatorService;
import ee.lio.birthcalculator.service.SessionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class BenefitController {

    private final CalculatorService calculatorService;
    private final SessionService sessionService;

    public BenefitController(CalculatorService calculatorService,
                             SessionService sessionService) {
        this.calculatorService = calculatorService;
        this.sessionService = sessionService;
    }

    @PostMapping(value = "/session")
    public Map<String, String> createSession() {
        String sessionId = sessionService.cre;
    }

    @PostMapping(value = "/calculator")
    public ResponseEntity<ApiResponse> calculate(@RequestBody BenefitRequest request) {
        Double grossSalary = Double.parseDouble(request.salary());
        LocalDate birthDate = LocalDate.parse(request.dob());
        List<Map<String, Object>> data = calculatorService.calculate(grossSalary,
                birthDate);
        return ResponseEntity.ok(new ApiResponse("Yearly breakdown calculated",
                data));
    }
}
