package ee.lio.birthcalculator.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record CalculationResult(
        boolean capped,
        BigDecimal capAmount,
        List<BenefitMonth> months
) {

}
