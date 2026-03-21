import {TestBed} from '@angular/core/testing';

import {BenefitService} from './benefit-service';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {ENVIRONMENT} from '@common/environment';
import {provideHttpClient} from '@angular/common/http';
import {BenefitRequest} from '@common/benefit-request';

describe('BenefitService', () => {
  let service: BenefitService;
  let httpMock: HttpTestingController;
  const apiUrl = ENVIRONMENT.apiUrl;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        BenefitService,
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    });
    service = TestBed.inject(BenefitService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('createSession should POST and return sessionId', () => {
    const mockResponse = {
      message: 'Session created',
      data: 'test-session-123'
    };

    service.createSession().subscribe(res => {
      expect(res.message).toBe('Session created');
      expect(res.data).toBe('test-session-123');
    });

    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual({});
    req.flush(mockResponse);
  });

  it('getSession should GET correct url', () => {
    const sessionId = 'test-session-123';
    const mockResponse = {
      message: 'Your session',
      data: {
        salary: 12345.0,
        dob: '2025-02-19'
      }
    };

    service.getSession(sessionId).subscribe(res => {
      expect(res.message).toBe('Your session');
      expect(res.data.salary).toBe(12345.0);
      expect(res.data.dob).toBe('2025-02-19');
    });

    const req = httpMock.expectOne(`${apiUrl}/${sessionId}`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });

  it('saveSession should POST with data', () => {
    const sessionId = 'test-session-123';
    const payload: BenefitRequest = {salary: 12345.0, dob: '2025-02-19'};
    const mockResponse = {message: 'Session saved', data: null};

    service.saveSession(sessionId, payload).subscribe(res => {
      expect(res).toEqual(mockResponse);
    });

    const req = httpMock.expectOne(`${apiUrl}/${sessionId}`);
    expect(req.request.method).toBe('POST');
    expect(req.request.body).toEqual(payload);
    req.flush(mockResponse);
  });

  it('calculate should GET calculator endpoint with correct data', () => {
    const sessionId = 'test-session-123';
    const mockResponse = {
      message: 'Yearly breakdown calculated',
      data: {
        capped: false,
        capAmount: 0,
        months: [
          {month: 'January', start: '2025-01-01', end: '2025-01-31', days: 31, payment: 1500.50},
          {month: 'February', start: '2025-02-01', end: '2025-02-28', days: 28, payment: 1350.75}
        ]
      }
    };

    service.calculate(sessionId).subscribe(res => {
      expect(res.message).toBe('Yearly breakdown calculated');
      expect(res.data.capped).toBe(false);
      expect(res.data.months).toHaveLength(2);
      expect(res.data.months[0].month).toBe('January');
      expect(res.data.months[0].payment).toBe(1500.50);
    });

    const req = httpMock.expectOne(`${apiUrl}/${sessionId}/calculator`);
    expect(req.request.method).toBe('GET');
    req.flush(mockResponse);
  });
});
