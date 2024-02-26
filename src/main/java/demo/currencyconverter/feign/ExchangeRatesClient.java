package demo.currencyconverter.feign;

import demo.currencyconverter.dto.ExchangeApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.util.Map;

@FeignClient(name = "exchange-rates-client", url = "${api.exchange-rates.base-url}")
public interface ExchangeRatesClient {
    @GetMapping("/latest")
    Map<String, Object> getLatestRates(@RequestParam("access_key") String accessKey, @RequestParam("base") String base);

    @GetMapping("/convert")
    ExchangeApiResponse convert(@RequestParam("access_key") String accessKey, @RequestParam("from") String from,
                                @RequestParam("to"  ) String to, @RequestParam("amount") BigDecimal amount);

}
