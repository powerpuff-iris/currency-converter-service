package demo.currencyconverter.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import demo.currencyconverter.dto.ConversionResponse;
import demo.currencyconverter.dto.ExchangeApiResponse;
import demo.currencyconverter.feign.ExchangeRatesClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

/**
 * Service class for currency conversion operations.
 */
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "exchangeRates")
public class ConverterService {

    @Value("${api.exchangerates.api-key}")
    private String apiKey;
    @Autowired
    protected ExchangeRatesClient exchangeRatesClient;

    /**
     * Converts the given amount from one currency to another.
     *
     */
    @HystrixCommand(fallbackMethod = "handleConversionFailure")
    public ConversionResponse converter(String baseCurrency, String targetCurrency, BigDecimal amount) {
        ExchangeApiResponse result = exchangeRatesClient.convert(apiKey, baseCurrency, targetCurrency, amount);
        return ConversionResponse.builder()
                .from(baseCurrency.toUpperCase())
                .to(targetCurrency.toUpperCase())
                .amount(amount)
                .convertedAmount(result.getResult())
                .build();
    }

    /**
     * Retrieves the exchange rates for the specified base currency and date.
     *
     * @param baseCurrency the base currency code
     * @param date         the date for which to retrieve the rates
     * @return the exchange rates
     */
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Map<String, Object> getExchangeRates(String baseCurrency, LocalDate date) {
        return exchangeRatesClient.getLatestRates(apiKey, baseCurrency.toUpperCase());
    }

    /**
     * Handles the failure of currency conversion and returns a fallback response.
     *
     * @param baseCurrency   the base currency code
     * @param targetCurrency the target currency code
     * @param amount         the amount to convert
     * @return the fallback conversion response
     */
    public ConversionResponse handleConversionFailure(String baseCurrency, String targetCurrency, BigDecimal amount) {
        Map<String, Object> ratesMap = getExchangeRates(baseCurrency, LocalDate.now());
        Map<String, Double> rates = (Map<String, Double>) ratesMap.get("rates");
        Double rate = rates.get(targetCurrency.toUpperCase());

        BigDecimal convertedAmount = BigDecimal.ZERO;
        if (rate != null) {
            convertedAmount = amount.multiply(BigDecimal.valueOf(rate));
        }

        return ConversionResponse.builder()
                .from(baseCurrency)
                .to(targetCurrency)
                .amount(amount)
                .convertedAmount(convertedAmount)
                .errorMessage("Failed to convert currency from " + baseCurrency + " to " + targetCurrency)
                .build();
    }
}
