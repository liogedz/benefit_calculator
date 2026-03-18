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
});
