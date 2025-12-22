package com.twochickendevs.bakeryuserservice.auth.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.twochickendevs.bakeryuserservice.auth.model.CacheKey;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CaffeineCacheConfig {

    @Bean
    public CaffeineCacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.registerCustomCache(CacheKey.GOOGLE_STATE,
                createCache(CacheKey.GOOGLE_STATE, 1, TimeUnit.MINUTES));
        return cacheManager;
    }

    private Cache<Object, Object> createCache(String cacheName, long duration, TimeUnit unit) {
        Caffeine<Object, Object> caffeine = Caffeine.newBuilder()
                .expireAfterWrite(duration, unit)
                .maximumSize(10000)
                .recordStats();

        return new CaffeineCache(cacheName, caffeine.build()).getNativeCache();
    }
}
