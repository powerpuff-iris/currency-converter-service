package demo.currencyconverter.service;

import demo.currencyconverter.dto.ConversionResponse;
import demo.currencyconverter.dto.ExchangeApiResponse;
import demo.currencyconverter.feign.ExchangeRatesClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ConverterServiceTests {
    @Mock
    private ExchangeRatesClient exchangeRatesClient;

    @InjectMocks
    private ConverterService converterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ReflectionTestUtils.setField(converterService, "apiKey", "test_api_key");
    }

    @Test
    public void testConverter() {
        // Mocking
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        ExchangeApiResponse mockApiResponse = new ExchangeApiResponse();
        mockApiResponse.setResult(BigDecimal.valueOf(85.0)); // Mocked conversion result
        when(exchangeRatesClient.convert(anyString(), eq(baseCurrency), eq(targetCurrency), eq(amount)))
                .thenReturn(mockApiResponse);

        // Execution
        ConversionResponse response = converterService.converter(baseCurrency, targetCurrency, amount);

        // Verification
        assertEquals("USD", response.getFrom());
        assertEquals("EUR", response.getTo());
        assertEquals(BigDecimal.valueOf(100), response.getAmount());
        assertEquals(BigDecimal.valueOf(85.0), response.getConvertedAmount());
    }

    @Test
    public void testHandleConversionFailure() {
        // Mocking
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        Map<String, Object> mockRatesMap = new HashMap<>();
        mockRatesMap.put("rates", new HashMap<String, Double>() {{
            put("EUR", 0.85); // Mocked conversion rate
        }});
        when(exchangeRatesClient.getLatestRates(anyString(), eq("USD"))).thenReturn(mockRatesMap);

        // Execution
        ConversionResponse response = converterService.handleConversionFailure(baseCurrency, targetCurrency, amount);
        BigDecimal b = BigDecimal.valueOf(85.00);
        // Verification
        assertEquals("USD", response.getFrom());
        assertEquals("EUR", response.getTo());
        assertEquals(BigDecimal.valueOf(100), response.getAmount());
        assertEquals(BigDecimal.valueOf(85.00).setScale(2, RoundingMode.HALF_UP), response.getConvertedAmount());
        assertEquals("Failed to convert currency from USD to EUR", response.getErrorMessage());
    }

    @Test
    public void testGetExchangeRates() {
        // Mocking
        String baseCurrency = "USD";
        LocalDate date = LocalDate.now();
        Map<String, Object> mockRatesMap = new HashMap<>();
        mockRatesMap.put("rates", new HashMap<String, Double>() {{
            put("EUR", 0.85); // Mocked conversion rate
        }});
        when(exchangeRatesClient.getLatestRates(anyString(), eq("USD"))).thenReturn(mockRatesMap);

        // Execution
        Map<String, Object> response = converterService.getExchangeRates(baseCurrency, date);

        // Verification
        assertEquals(0.85, ((Map<String, Double>) response.get("rates")).get("EUR"));
    }
}
