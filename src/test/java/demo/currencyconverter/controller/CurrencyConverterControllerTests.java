package demo.currencyconverter.controller;

import demo.currencyconverter.dto.ConversionResponse;
import demo.currencyconverter.service.ConverterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurrencyConverterControllerTests {
    @Mock
    ConverterService converterService;
    @InjectMocks
    CurrencyConverterController controller;

    @Test
    void convert_Success() {
        // Arrange
        String baseCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        ConversionResponse conversionResponse = mock(ConversionResponse.class); // Mock or create a real instance as needed
        when(converterService.converter(baseCurrency, targetCurrency, amount)).thenReturn(conversionResponse);

        // Act
        ResponseEntity<ConversionResponse> responseEntity = controller.convert(baseCurrency, targetCurrency, amount);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(conversionResponse, responseEntity.getBody());
        verify(converterService, times(1)).converter(baseCurrency, targetCurrency, amount);
    }
}
