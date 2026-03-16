import {Component, computed, OnInit, signal} from '@angular/core';
import {BenefitService} from '@services/benefit-service';
import {BenefitMonth} from '@common/benefit-month';
import {BenefitRequest} from '@common/benefit-request';
import {form, FormField, min, required, validate} from '@angular/forms/signals';
import {switchMap} from 'rxjs';

@Component({
  selector: 'app-calculator-component',
  imports: [
    FormField
  ],
  templateUrl: './calculator-component.html',
  styleUrl: './calculator-component.css',
})
export class CalculatorComponent implements OnInit {
  sessionId!: string;
  result = signal<BenefitMonth[]>([]);

  constructor(private service: BenefitService) {
  }

  ngOnInit() {
    this.service.createSession().subscribe(res => {
      this.sessionId = res.sessionId;
    });
  }

  benefitModel = signal<BenefitRequest>({
    salary: 0,
    dob: ''
  });
  benefitForm = form(this.benefitModel, (fieldPath) => {
    required(fieldPath.salary, {message: 'Salary is required'});
    min(fieldPath.salary, 100, {message: 'Minimum salary is 100 euro'});
    required(fieldPath.dob, {message: 'Date of Birth is required'});
    validate(fieldPath.dob, ({value}) => {

      const dob = value();
      if (!dob) return null;
      const selected = new Date(dob);
      const today = new Date();
      if (selected > today) {
        return {
          kind: 'futureDate',
          message: 'Date of birth cannot be in the future'
        };
      }

      return null;
    });
  });

  hasFormErrors = computed(() =>
    this.benefitForm.salary().invalid() ||
    this.benefitForm.dob().invalid()
  );

  formTouched = computed(() =>
    this.benefitForm.salary().touched() ||
    this.benefitForm.dob().touched()
  );

  isFormDisabled = computed(() =>
    this.hasFormErrors() && this.formTouched()
  );

  onCalculateSubmit(event: Event) {
    event.preventDefault();

    this.service.saveSession(this.sessionId, this.benefitModel())
      .pipe(
        switchMap(() => this.service.calculate(this.sessionId))
      )
      .subscribe(res => this.result.set(res.data));
  }

  totalPayment = computed(() =>
    this.result().reduce((sum, m) => sum + m.payment, 0)
  );
}
