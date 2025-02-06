package util;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    @SuppressWarnings("rawtypes")
    public Caffeine caffeineConfig() {
        return Caffeine.newBuilder()
            .maximumSize(10000)
            .expireAfterAccess(10, TimeUnit.MINUTES)
            .expireAfterWrite(5, TimeUnit.MINUTES);
    }

    @Bean
    @SuppressWarnings("unchecked")
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("UpperCumulativeFrequencies", "LowerCumulativeFrequencies");
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }
}
