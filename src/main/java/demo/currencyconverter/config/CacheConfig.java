package demo.currencyconverter.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cache.interceptor.KeyGenerator;
import java.lang.reflect.Method;
import java.time.LocalDate;

@Configuration
@EnableCaching
public class CacheConfig {
    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                String baseCurrency = (String) params[0];
                LocalDate date = (LocalDate) params[1];
                return baseCurrency + "-" + date.toString();
            }
        };
    }
}
