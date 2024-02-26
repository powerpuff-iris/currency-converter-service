package demo.currencyconverter.utils;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Currency;
import java.util.Set;
import java.util.stream.Collectors;

public class CurrencyCodeValidator implements ConstraintValidator<ValidCurrencyCode, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        // Check if the currency code is valid
        return getAllCurrencies().contains(value.toUpperCase());
    }

    public static Set<String> getAllCurrencies() {
        Set<String> currencies = Currency.getAvailableCurrencies()
                .stream()
                .map(Currency::getCurrencyCode)
                .collect(Collectors.toSet());
        return currencies;
    }
}