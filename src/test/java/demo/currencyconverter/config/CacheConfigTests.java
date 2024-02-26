package demo.currencyconverter.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.interceptor.KeyGenerator;

import java.time.LocalDate;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class CacheConfigTests {
    @Test
    public void testKeyGenerator() {
        // Arrange
        CacheConfig cacheConfig = new CacheConfig();
        KeyGenerator keyGenerator = cacheConfig.keyGenerator();
        String baseCurrency = "USD";
        LocalDate date = LocalDate.of(2024, 2, 26);

        // Act
        Object generatedKey = keyGenerator.generate(null, null, baseCurrency, date);

        // Assert
        assertNotNull(generatedKey);
        assertTrue(generatedKey instanceof String);
        assertEquals("USD-2024-02-26", generatedKey);
    }
}
