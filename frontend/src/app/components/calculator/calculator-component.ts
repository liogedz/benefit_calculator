import {Component, computed, OnInit, signal} from '@angular/core';
import {BenefitService} from '@services/benefit-service';
import {BenefitMonth} from '@common/benefit-month';
import {BenefitRequest} from '@common/benefit-request';
import {form, FormField, FormRoot, min, required, validate} from '@angular/forms/signals';
import {firstValueFrom} from 'rxjs';
import {CurrencyPipe, NgClass} from '@angular/common';

@Component({
  selector: 'app-calculator-component',
  imports: [
    FormField,
    CurrencyPipe,
    NgClass,
    FormRoot
  ],
  templateUrl: './calculator-component.html',
  styleUrl: './calculator-component.css',
})
export class CalculatorComponent implements OnInit {
  sessionId!: string;
  result = signal<BenefitMonth[]>([]);
  isCapped = signal<boolean>(false);

  constructor(private service: BenefitService) {
  }

  ngOnInit() {
    const storedSession = localStorage.getItem('benefitSession');
    if (storedSession) {
      this.sessionId = storedSession;
      this.service.getSession(this.sessionId)
        .subscribe((res => this.benefitModel.set(res.data)));
    } else {
      this.service.createSession().subscribe(res => {
        this.sessionId = res.data;
        localStorage.setItem('benefitSession', this.sessionId);
      });
    }
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
        today.setHours(0, 0, 0, 0);

        const maxAgeDate = new Date();
        maxAgeDate.setFullYear(maxAgeDate.getFullYear() - 3);

        if (selected < maxAgeDate) {
          return {
            kind: 'tooOld',
            message: 'Child must be younger than 3 years'
          };
        }
        return null;
      });
    },
    {
      submission: {
        action: async f => {
          if (this.result().length) {
            this.result.set([]);
            return;
          }
          const value = f().value();
          await firstValueFrom(this.service.saveSession(this.sessionId, value));
          const res = await firstValueFrom(this.service.calculate(this.sessionId))

          this.result.set(res.data.months);
          this.isCapped.set(res.data.capped);
        }
      }
    }
  );

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

  hasResult = computed(() => this.result().length > 0);

  totalPayment = computed(() =>
    this.result().reduce((sum, m) => sum + m.payment, 0)
  );

}
