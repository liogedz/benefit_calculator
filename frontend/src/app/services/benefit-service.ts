import {Injectable} from '@angular/core';
import {ENVIRONMENT} from '@common/environment';
import {HttpClient} from '@angular/common/http';
import {BenefitRequest} from '@common/benefit-request';
import {ApiResponse} from '@common/api-response';
import {CalculationResult} from '@common/calculation-result';

@Injectable({
  providedIn: 'root',
})
export class BenefitService {
  private apiUrl = `${ENVIRONMENT.apiUrl}`

  constructor(private http: HttpClient) {
  }

  createSession() {
    return this.http.post<ApiResponse<string>>(this.apiUrl, {});
  }

  saveSession(sessionId: string, data: BenefitRequest) {
    return this.http.post<ApiResponse<void>>(`${this.apiUrl}/${sessionId}`, data);
  }

  calculate(sessionId: string) {
    return this.http.get<ApiResponse<CalculationResult>>(`${this.apiUrl}/${sessionId}/calculator`);
  }

  getSession(sessionId: string) {
    return this.http.get<ApiResponse<BenefitRequest>>(`${this.apiUrl}/${sessionId}`);
  }
}
