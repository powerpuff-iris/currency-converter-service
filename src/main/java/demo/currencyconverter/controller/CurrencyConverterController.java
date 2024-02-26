package demo.currencyconverter.controller;

import demo.currencyconverter.dto.ConversionResponse;
import demo.currencyconverter.service.ConverterService;
import demo.currencyconverter.utils.ValidCurrencyCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;

/**
 * This class represents the controller for the Currency Converter API.
 * It handles the conversion of an amount from one currency to another.
 */
@Tag(name = "Converter API")
@RestController
@CrossOrigin
@AllArgsConstructor
public class CurrencyConverterController {
    @Autowired
    private ConverterService converterService;

    /**
     * Converts an amount from one currency to another.
     *
     * @param baseCurrency   The base currency code.
     * @param targetCurrency The target currency code.
     * @param amount         The amount to be converted.
     * @return The conversion response containing the converted amount.
     */
    @GetMapping("/converter")
    @ResponseStatus(code = HttpStatus.OK)
    @Operation(description = "Converts an amount from one currency to another")
    public ResponseEntity<ConversionResponse> convert(@RequestParam @ValidCurrencyCode String baseCurrency,
                                                      @RequestParam @ValidCurrencyCode String targetCurrency,
                                                      @RequestParam @NotNull(message = "Amount cannot be null") BigDecimal amount
    ) {
        return ResponseEntity.ok(converterService.converter(baseCurrency, targetCurrency, amount));
    }

}
