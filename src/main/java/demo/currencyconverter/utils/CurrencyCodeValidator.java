package demo.currencyconverter.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The CurrencyCodeValidator class is responsible for validating currency codes.
 */
public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    /**
     * Checks if the given currency code is valid.
     *
     */
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Check if the currency code is valid
        return getAllCurrencies().contains(value.toUpperCase());
    }

    /**
     * Retrieves a set of all available currency codes.
     *
     */
    public static Set<String> getAllCurrencies() {
        Set<String> currencies = Currency.getAvailableCurrencies()
                .stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
        return currencies;
    }
}