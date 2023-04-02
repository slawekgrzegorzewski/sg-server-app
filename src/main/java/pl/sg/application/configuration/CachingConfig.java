package pl.sg.application.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@EnableCaching
public class CachingConfig {

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("application_users");
    }
}
