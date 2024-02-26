package demo.currencyconverter.dto;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class ExchangeApiResponseTests {
    @Test
    public void testSuccess() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        response.setSuccess(true);
        assertTrue(response.isSuccess());
    }

    @Test
    public void testQuery() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        ExchangeApiResponse.Query query = new ExchangeApiResponse.Query();
        query.setFrom("USD");
        query.setTo("EUR");
        query.setAmount(BigDecimal.valueOf(100));
        response.setQuery(query);
        assertEquals("USD", response.getQuery().getFrom());
        assertEquals("EUR", response.getQuery().getTo());
        assertEquals(BigDecimal.valueOf(100), response.getQuery().getAmount());
    }

    @Test
    public void testInfo() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        ExchangeApiResponse.Info info = new ExchangeApiResponse.Info();
        info.setTimestamp(1234567890L);
        info.setRate(BigDecimal.valueOf(1.5));
        response.setInfo(info);
        assertEquals(1234567890L, response.getInfo().getTimestamp());
        assertEquals(BigDecimal.valueOf(1.5), response.getInfo().getRate());
    }

    @Test
    public void testHistorical() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        response.setHistorical("2024-02-25");
        assertEquals("2024-02-25", response.getHistorical());
    }

    @Test
    public void testDate() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        response.setDate("2024-02-26");
        assertEquals("2024-02-26", response.getDate());
    }

    @Test
    public void testResult() {
        ExchangeApiResponse response = new ExchangeApiResponse();
        response.setResult(BigDecimal.valueOf(150));
        assertEquals(BigDecimal.valueOf(150), response.getResult());
    }
}
