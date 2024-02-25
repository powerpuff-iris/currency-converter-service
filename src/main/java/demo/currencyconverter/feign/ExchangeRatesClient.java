package demo.currencyconverter.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(name = "exchange-rates-client", url = "${exchange.api.url}")
public interface ExchangeRatesClient {
    @GetMapping("/latest")
    Map<String, Object> getLatestRates(@RequestParam String base);
}
