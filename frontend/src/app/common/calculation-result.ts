import {BenefitMonth} from '@common/benefit-month';

export interface CalculationResult {
  capped: boolean;
  capAmount: number;
  months: BenefitMonth[];
}
