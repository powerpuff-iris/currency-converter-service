package demo.currencyconverter.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ConversionResponseTests {
    @Test
    public void testBuilder() {
        // Arrange
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal convertedAmount = BigDecimal.valueOf(85);
        String errorMessage = "Conversion failed";

        // Act
        ConversionResponse response = ConversionResponse.builder()
                .from(from)
                .to(to)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .errorMessage(errorMessage)
                .build();

        // Assert
        assertNotNull(response);
        assertEquals(from, response.getFrom());
        assertEquals(to, response.getTo());
        assertEquals(amount, response.getAmount());
        assertEquals(convertedAmount, response.getConvertedAmount());
        assertEquals(errorMessage, response.getErrorMessage());
    }

    @Test
    public void testGetterSetter() {
        // Arrange
        ConversionResponse response =  ConversionResponse.builder().build();
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal convertedAmount = BigDecimal.valueOf(85);
        String errorMessage = "Conversion failed";

        // Act
        response.setFrom(from);
        response.setTo(to);
        response.setAmount(amount);
        response.setConvertedAmount(convertedAmount);
        response.setErrorMessage(errorMessage);

        // Assert
        assertNotNull(response);
        assertEquals(from, response.getFrom());
        assertEquals(to, response.getTo());
        assertEquals(amount, response.getAmount());
        assertEquals(convertedAmount, response.getConvertedAmount());
        assertEquals(errorMessage, response.getErrorMessage());
    }

    @Test
    public void testToString() {
        // Arrange
        String from = "USD";
        String to = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);
        BigDecimal convertedAmount = BigDecimal.valueOf(85);
        String errorMessage = "Conversion failed";
        ConversionResponse response = ConversionResponse.builder()
                .from(from)
                .to(to)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .errorMessage(errorMessage)
                .build();

        // Act
        String str = response.toString();

        // Assert
        assertNotNull(str);
        assertTrue(str.contains(from));
        assertTrue(str.contains(to));
        assertTrue(str.contains(amount.toString()));
        assertTrue(str.contains(convertedAmount.toString()));
        assertTrue(str.contains(errorMessage));
    }
}
