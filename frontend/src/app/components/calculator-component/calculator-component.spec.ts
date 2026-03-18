import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CalculatorComponent} from './calculator-component';
import {provideHttpClient} from '@angular/common/http';
import {HttpTestingController, provideHttpClientTesting} from '@angular/common/http/testing';
import {ENVIRONMENT} from '@common/environment';

describe('CalculatorComponent', () => {
  let component: CalculatorComponent;
  let fixture: ComponentFixture<CalculatorComponent>;
  let httpMock: HttpTestingController;
  const apiUrl = ENVIRONMENT.apiUrl;

  beforeEach(async () => {
    localStorage.clear(); // always start clean → forces createSession branch

    await TestBed.configureTestingModule({
      imports: [CalculatorComponent],
      providers: [
        provideHttpClient(),
        provideHttpClientTesting(),
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CalculatorComponent);
    component = fixture.componentInstance;
    httpMock = TestBed.inject(HttpTestingController);

    fixture.detectChanges(); // triggers ngOnInit → goes to else branch

    // createSession posts to apiUrl with no sessionId segment
    const req = httpMock.expectOne(apiUrl);
    expect(req.request.method).toBe('POST');
    req.flush({data: 'test-session-123'});

    await fixture.whenStable();
  });

  afterEach(() => {
    localStorage.clear();
    httpMock.verify();
  });

  it('should load existing session from localStorage', () => {
    localStorage.setItem('benefitSession', 'existing-session-id');

    fixture = TestBed.createComponent(CalculatorComponent);
    fixture.detectChanges();

    const req = httpMock.expectOne(`${apiUrl}/existing-session-id`);
    expect(req.request.method).toBe('GET');
    req.flush({data: { /* your BenefitMonth[] mock */}});
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should render title', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h2')?.textContent).toContain('Parental Benefit Calculator');
  });
});
