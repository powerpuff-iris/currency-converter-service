package demo.currencyconverter.service.impl;

import demo.currencyconverter.dto.ConversionResponse;
import demo.currencyconverter.dto.ExchangeRatesResponse;
import demo.currencyconverter.service.ConverterService;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;

@Service
public class ConverterServiceImpl implements ConverterService {

    @Value("${api.exchangerates.api-key}")
    private String apiKey;
    @Value("${api.exchangerates.base-url}")
    private String baseUrl;
    private final CircuitBreaker circuitBreaker;

    public ConverterServiceImpl(CircuitBreaker circuitBreaker) {
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public ConversionResponse convert(String baseCurrency, String targetCurrency, BigDecimal amount) {
        String url = UriComponentsBuilder.fromHttpUrl(baseUrl)
                .path("latest")
                .queryParam("access_key", apiKey)
                .queryParam("base", baseCurrency)
                .queryParam("symbols", targetCurrency)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        ExchangeRatesResponse response;

        response = circuitBreaker.executeSupplier(() -> restTemplate.getForObject(url, ExchangeRatesResponse.class));

        ConversionResponse resp;

        if(response.isSuccess()){
            resp = ConversionResponse.builder()
                    .from(baseCurrency)
                    .to(targetCurrency)
                    .amount(amount)
                    .convertedAmount(BigDecimal.valueOf(response.getRates().get(targetCurrency)).multiply(amount))
                    .build();
        }else {
            resp = ConversionResponse.builder()
                    .from(baseCurrency)
                    .to(targetCurrency)
                    .amount(amount)
                    .errorMessage(response.getError().getInfo())
                    .build();
        }
        return resp;
    }
}
