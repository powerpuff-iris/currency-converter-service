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

@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "exchangeRates")
public class ConverterService {

    @Value("${api.exchangerates.api-key}")
    private String apiKey;
    @Autowired
    protected ExchangeRatesClient exchangeRatesClient;

    // v2.0 - calling the https://api.exchangeratesapi.io/v1/convert?access_key=apiKey&from=baseCurrency&to=targetCurrency&amount=amount
    // accepts all currencies as base currency and get the converted amount in the response
    @HystrixCommand(fallbackMethod = "handleConversionFailure")
    public  ConversionResponse converter(String baseCurrency, String targetCurrency, BigDecimal amount) {
        ExchangeApiResponse result = exchangeRatesClient.convert( apiKey, baseCurrency, targetCurrency, amount);
        return ConversionResponse.builder()
                .from(baseCurrency.toUpperCase())
                .to(targetCurrency.toUpperCase())
                .amount(amount)
                .convertedAmount(result.getResult())
                .build();
    }

    // https://api.exchangeratesapi.io/v1/latest?access_key=apiKey for latest rates
    @Cacheable(keyGenerator = "customKeyGenerator")
    public Map<String, Object> getExchangeRates(String baseCurrency, LocalDate date) {
        return exchangeRatesClient.getLatestRates(apiKey, baseCurrency.toUpperCase());
    }

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

//     v1.0 - calling the getExchangeRates and converting it manually
//     limitation - accepts as base currency only EUR
   /* @HystrixCommand(
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
            },
            fallbackMethod = "handleConversionFailure")
    public ConversionResponse convertCurrency(String baseCurrency, String targetCurrency, BigDecimal amount) {
        if (baseCurrency.equalsIgnoreCase(targetCurrency)) {
            return ConversionResponse.builder()
                    .from(baseCurrency.toUpperCase())
                    .to(targetCurrency.toUpperCase())
                    .amount(amount)
                    .convertedAmount(amount) // No conversion needed, so the converted amount is the same as the input amount
                    .build();
        }
        Map<String, Object> ratesMap = getExchangeRates(baseCurrency);
        Map<String, Double> rates = (Map<String, Double>) ratesMap.get("rates");
        Double rate =  rates.get(targetCurrency.toUpperCase());

        return ConversionResponse.builder()
                .from(baseCurrency.toUpperCase())
                .to(targetCurrency.toUpperCase())
                .amount(amount)
                .convertedAmount(amount.multiply(BigDecimal.valueOf(rate)))
                .build();
    }*/
}
