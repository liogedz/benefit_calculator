import {Injectable} from '@angular/core';
import {ENVIRONMENT} from '@common/environment';
import {HttpClient} from '@angular/common/http';
import {BenefitRequest} from '@common/benefit-request';

@Injectable({
  providedIn: 'root',
})
export class BenefitService {
  private apiUrl = `${ENVIRONMENT.apiUrl}`

  constructor(private http: HttpClient) {
  }

  createSession() {
    return this.http.post<{ sessionId: string }>(this.apiUrl, {});
  }

  saveSession(sessionId: string, data: BenefitRequest) {
    return this.http.post(`${this.apiUrl}/${sessionId}`, data);
  }

  calculate(sessionId: string) {
    return this.http.get<any>(`${this.apiUrl}/${sessionId}/calculator`);
  }

  getSession(sessionId: string) {
    return this.http.get(`${this.apiUrl}/${sessionId}`);
  }
}
