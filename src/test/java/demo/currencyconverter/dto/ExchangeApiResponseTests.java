package demo.currencyconverter.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class ExchangeApiResponseTests {
    @Test
    public void testGetterSetter() {
        // Arrange
        ExchangeApiResponse exchangeApiResponse = new ExchangeApiResponse();
        boolean success = true;
        String historical = "2024-02-25";
        String date = "2024-02-26";
        BigDecimal result = BigDecimal.valueOf(85);
        long timestamp = System.currentTimeMillis();
        BigDecimal rate = BigDecimal.valueOf(0.85);
        BigDecimal amount = BigDecimal.valueOf(100);
        String from = "USD";
        String to = "EUR";

        // Act
        exchangeApiResponse.setSuccess(success);
        exchangeApiResponse.setHistorical(historical);
        exchangeApiResponse.setDate(date);
        exchangeApiResponse.setResult(result);

        ExchangeApiResponse.Query query = new ExchangeApiResponse.Query();
        query.setFrom(from);
        query.setTo(to);
        query.setAmount(amount);
        exchangeApiResponse.setQuery(query);

        ExchangeApiResponse.Info info = new ExchangeApiResponse.Info();
        info.setTimestamp(timestamp);
        info.setRate(rate);
        exchangeApiResponse.setInfo(info);

        // Assert
        assertNotNull(exchangeApiResponse);
        assertEquals(success, exchangeApiResponse.isSuccess());
        assertEquals(historical, exchangeApiResponse.getHistorical());
        assertEquals(date, exchangeApiResponse.getDate());
        assertEquals(result, exchangeApiResponse.getResult());
        assertNotNull(exchangeApiResponse.getQuery());
        assertEquals(from, exchangeApiResponse.getQuery().getFrom());
        assertEquals(to, exchangeApiResponse.getQuery().getTo());
        assertEquals(amount, exchangeApiResponse.getQuery().getAmount());
        assertNotNull(exchangeApiResponse.getInfo());
        assertEquals(timestamp, exchangeApiResponse.getInfo().getTimestamp());
        assertEquals(rate, exchangeApiResponse.getInfo().getRate());
    }
}
