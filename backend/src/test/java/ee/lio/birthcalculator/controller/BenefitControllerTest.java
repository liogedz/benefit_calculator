package ee.lio.birthcalculator.controller;

import ee.lio.birthcalculator.dto.request.BenefitRequest;
import ee.lio.birthcalculator.dto.response.BenefitMonth;
import ee.lio.birthcalculator.dto.response.CalculationResult;
import ee.lio.birthcalculator.model.BenefitSession;
import ee.lio.birthcalculator.service.CalculatorService;
import ee.lio.birthcalculator.service.SessionService;
import org.jspecify.annotations.NonNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BenefitController.class)
class BenefitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private SessionService sessionService;

    @MockitoBean
    private CalculatorService calculatorService;

    private final static BigDecimal TEST_VALUE = new BigDecimal("12345.0");
    private final static BigDecimal EXPECT_PAYMENT_1 = new BigDecimal("1500.50");
    private final static BigDecimal EXPECT_PAYMENT_2 = new BigDecimal("1350.75");
    private final static BigDecimal CAP_AMOUNT = new BigDecimal("4000.00");

    @Test
    void createSession_shouldReturnSessionId() throws Exception {
        when(sessionService.createSession()).thenReturn("test-session-123");

        mockMvc.perform(post("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Session created"))
                .andExpect(jsonPath("$.data").value("test-session-123"));
    }

    @Test
    void saveSession_shouldReturnSuccess() throws Exception {

        BenefitRequest request = new BenefitRequest(TEST_VALUE,
                LocalDate.of(2025,
                        2,
                        19));

        mockMvc.perform(post("/api/session/test-session-123")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Session saved"))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(sessionService).saveSessionData("test-session-123",
                TEST_VALUE,
                LocalDate.of(2025,
                        2,
                        19));
    }

    @Test
    void getSession_shouldReturnSessionData() throws Exception {
        BenefitRequest sessionData = new BenefitRequest(TEST_VALUE,
                LocalDate.of(2025,
                        2,
                        19));

        when(sessionService.getSessionData("test-session-123")).thenReturn(sessionData);

        mockMvc.perform(get("/api/session/test-session-123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Your session"))
                .andExpect(jsonPath("$.data.salary").value(TEST_VALUE))
                .andExpect(jsonPath("$.data.dob").value("2025-02-19"));
    }

    @Test
    void calculateSession_shouldReturnBreakdown() throws Exception {

        BenefitSession session = new BenefitSession();
        session.setGrossSalary(TEST_VALUE);
        session.setBirthDate(LocalDate.of(2026,
                1,
                15));

        CalculationResult result = getCalculationResult();

        when(sessionService.getSession("test-session-123"))
                .thenReturn(session);

        when(calculatorService.calculate(
                TEST_VALUE,
                LocalDate.of(2026,
                        1,
                        15)))
                .thenReturn(result);

        mockMvc.perform(get("/api/session/test-session-123/calculator"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Yearly breakdown calculated"))
                .andExpect(jsonPath("$.data.capped").value(true))
                .andExpect(jsonPath("$.data.capAmount").value(4000.00))
                .andExpect(jsonPath("$.data.months").isArray())
                .andExpect(jsonPath("$.data.months[0].month").value("January"))
                .andExpect(jsonPath("$.data.months[0].days").value(31))
                .andExpect(jsonPath("$.data.months[0].payment").value(1500.50))
                .andExpect(jsonPath("$.data.months[1].month").value("February"));
    }

    private static @NonNull CalculationResult getCalculationResult() {
        List<BenefitMonth> months = List.of(
                new BenefitMonth("January",
                        LocalDate.of(2025,
                                1,
                                1),
                        LocalDate.of(2025,
                                1,
                                31),
                        31,
                        EXPECT_PAYMENT_1),
                new BenefitMonth("February",
                        LocalDate.of(2025,
                                2,
                                1),
                        LocalDate.of(2025,
                                2,
                                28),
                        28,
                        EXPECT_PAYMENT_2)
        );

        return new CalculationResult(
                true,
                CAP_AMOUNT,
                months
        );
    }
}
