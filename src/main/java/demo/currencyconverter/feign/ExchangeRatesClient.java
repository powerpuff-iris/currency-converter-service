package demo.currencyconverter.feign;

import demo.currencyconverter.dto.ExchangeApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

/**
 * This interface represents a Feign client for accessing exchange rates API.
 */
@FeignClient(name = "exchange-rates-client", url = "${api.exchange-rates.base-url}")
public interface ExchangeRatesClient {
    /**
     * Retrieves the latest exchange rates.
     *
     * @param accessKey the access key for the API
     * @param base      the base currency
     * @return a map containing the latest exchange rates
     */
    @GetMapping("/latest")
    Map<String, Object> getLatestRates(@RequestParam("access_key") String accessKey, @RequestParam("base") String base);

    /**
     * Converts an amount from one currency to another.
     *
     */
    @GetMapping("/convert")
    ExchangeApiResponse convert(@RequestParam("access_key") String accessKey, @RequestParam("from") String from,
                                @RequestParam("to") String to, @RequestParam("amount") BigDecimal amount);
}
