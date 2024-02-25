package demo.currencyconverter.dto;

import demo.currencyconverter.error.ErrorResponse;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class ExchangeRatesResponse {
    private boolean success;
    private long timestamp;
    private String base;
    private String date;
    private Map<String, Double> rates;
    private ErrorResponse error;
}
