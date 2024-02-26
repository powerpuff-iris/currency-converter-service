package demo.currencyconverter.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ExchangeApiResponse {
    @JsonProperty("success")
    private boolean success;
    @JsonProperty("query")
    private Query query;
    @JsonProperty("info")
    private Info info;
    @JsonProperty("historical")
    private String historical;
    @JsonProperty("date")
    private String date;
    @JsonProperty("result")
    private BigDecimal result;

    @Data
    static class Query {
        @JsonProperty("from")
        private String from;
        @JsonProperty("to")
        private String to;
        @JsonProperty("amount")
        private BigDecimal amount;
    }
    @Data
    static class Info {
        @JsonProperty("timestamp")
        private long timestamp;
        @JsonProperty("rate")
        private BigDecimal rate;
    }
}
