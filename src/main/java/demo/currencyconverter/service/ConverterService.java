package demo.currencyconverter.service;

import demo.currencyconverter.dto.ConversionResponse;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public interface ConverterService {
       ConversionResponse convert(String baseCurrency, String targetCurrency, BigDecimal amount);
}
