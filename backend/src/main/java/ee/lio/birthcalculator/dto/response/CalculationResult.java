package ee.lio.birthcalculator.dto.response;

import java.util.List;

public record CalculationResult(
        boolean capped,
        double capAmount,
        List<BenefitMonth> months
) {

}
