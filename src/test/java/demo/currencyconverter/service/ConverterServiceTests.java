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
import org.springframework.web.client.HttpServerErrorException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConverterServiceTests {
    @InjectMocks
    private ConverterService converterService;
    @Mock
    private ExchangeRatesClient exchangeRatesClient;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        converterService = new ConverterService();
        converterService.exchangeRatesClient = exchangeRatesClient;
    }

    @Test
    public void testConverter_Success() {
        // Arrange
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        ExchangeApiResponse mockResponse = new ExchangeApiResponse();
        mockResponse.setResult(BigDecimal.valueOf(85)); // Assuming 1 USD = 0.85 EUR
        when(exchangeRatesClient.convert(anyString(), eq(baseCurrency), eq(targetCurrency), eq(amount)))
                .thenReturn(mockResponse);

        // Act
        ConversionResponse response = converterService.converter(baseCurrency, targetCurrency, amount);

        // Assert
        assertNotNull(response);
        assertEquals(baseCurrency.toUpperCase(), response.getFrom());
        assertEquals(targetCurrency.toUpperCase(), response.getTo());
        assertEquals(amount, response.getAmount());
        assertEquals(BigDecimal.valueOf(85 * 100), response.getConvertedAmount());
    }

    @Test
    public void testConverter_Fallback() {
        // Arrange
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        when(exchangeRatesClient.convert(anyString(), eq(baseCurrency), eq(targetCurrency), eq(amount)))
                .thenThrow(new HttpServerErrorException(null));

        // Act
        ConversionResponse response = converterService.converter(baseCurrency, targetCurrency, amount);

        // Assert
        assertNotNull(response);
        assertEquals(baseCurrency, response.getFrom());
        assertEquals(targetCurrency, response.getTo());
        assertEquals(amount, response.getAmount());
        assertNotNull(response.getConvertedAmount());
        assertTrue(response.getErrorMessage().contains("Failed to convert currency"));
    }

    @Test
    public void testHandleConversionFailure_Success() {
        // Arrange
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        Map<String, Object> ratesMap = new HashMap<>();
        Map<String, Double> rates = new HashMap<>();
        rates.put(targetCurrency.toUpperCase(), 0.85); // Assuming 1 USD = 0.85 EUR
        ratesMap.put("rates", rates);
        when(exchangeRatesClient.getLatestRates(anyString(), eq(baseCurrency.toUpperCase()))).thenReturn(ratesMap);

        // Act
        ConversionResponse response = converterService.handleConversionFailure(baseCurrency, targetCurrency, amount);

        // Assert
        assertNotNull(response);
        assertEquals(baseCurrency, response.getFrom());
        assertEquals(targetCurrency, response.getTo());
        assertEquals(amount, response.getAmount());
        assertEquals(BigDecimal.valueOf(85 * 100), response.getConvertedAmount());

    }

    @Test
    public void testHandleConversionFailure_RateNotFound() {
        // Arrange
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        Map<String, Object> ratesMap = new HashMap<>();
        when(exchangeRatesClient.getLatestRates(anyString(), eq(baseCurrency.toUpperCase()))).thenReturn(ratesMap);

        // Act
        ConversionResponse response = converterService.handleConversionFailure(baseCurrency, targetCurrency, amount);

        // Assert
        assertNotNull(response);
        assertEquals(baseCurrency, response.getFrom());
        assertEquals(targetCurrency, response.getTo());
        assertEquals(amount, response.getAmount());
        assertEquals(BigDecimal.ZERO, response.getConvertedAmount());
        assertTrue(response.getErrorMessage().contains("Failed to convert currency"));
    }

}
