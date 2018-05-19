package me.dong.springboot2rediscache.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.interceptor.CacheInterceptor;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.extern.slf4j.Slf4j;
import me.dong.springboot2rediscache.book.BookService;
import me.dong.springboot2rediscache.category.CategoryService;
import me.dong.springboot2rediscache.common.SampleProfiles;
import me.dong.springboot2rediscache.support.cache.CachedRestClient;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@EnableCaching(proxyTargetClass = true)
@Configuration
@Slf4j
public class CacheConfiguration {

    @Profile(SampleProfiles.CLOUD)
    static class CloudCacheConfiguration {

        @Value(CachedRestClient.CACHE_TTL)
        private Long cacheNetworkTimeToLive;

        @Value(BookService.CACHE_TTL)
        private Long cacheBookTimeToLive;

        @Value(CategoryService.CACHE_TTL)
        private Long cacheCategoryTimeToLive;

        @Bean
        public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
            Map<String, RedisCacheConfiguration> redisCacheConfigurations = new HashMap<>();
            redisCacheConfigurations.put(BookService.CACHE_NAME, createRedisCacheConfiguration(cacheBookTimeToLive));
            redisCacheConfigurations.put(CategoryService.CACHE_NAME, createRedisCacheConfiguration(cacheCategoryTimeToLive));

            // Use the default redisTemplate for caching REST calls
            redisCacheConfigurations.put(CachedRestClient.CACHE_NAME, createRedisCacheConfiguration(cacheNetworkTimeToLive));

            return RedisCacheManager.builder(
                    RedisCacheWriter.lockingRedisCacheWriter(redisConnectionFactory))
                    .withInitialCacheConfigurations(redisCacheConfigurations)
                    .build();
        }

        private RedisCacheConfiguration createRedisCacheConfiguration(long ttl) {
            return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(ttl));
        }
    }

    @Profile(SampleProfiles.STANDALONE)
    static class LocalCacheConfiguration {

        @Bean
        public CacheManager cacheManager() {
            SimpleCacheManager cacheManager = new SimpleCacheManager();
            cacheManager.setCaches(
                    Stream.of(BookService.CACHE_NAME, CategoryService.CACHE_NAME,
                            CachedRestClient.CACHE_NAME)
                            .map(ConcurrentMapCache::new)
                            .collect(Collectors.toList()));
            return cacheManager;
        }

        /**
         * Custom key generator
         *
         * @return
         */
        @Bean
        public KeyGenerator customKeyGenerator(CacheInterceptor cacheInterceptor) {
            KeyGenerator keyGenerator = (target, method, params) -> {
                StringBuilder sb = new StringBuilder();

                sb.append(target.getClass().getName());
                sb.append(method.getName());

                for (Object param : params) {
                    sb.append(param);
                }

                log.info("{}", sb.toString());

                return sb.toString();
            };

            // Bean으로 선언해도 이걸 안해주면 CacheAspectSupport L:92의 default SimpleKeyGenerator 사용하게 된다
            // 이방법이 아니라면 @Cacheable(keyGenerator="")에 선언해야 한다
            cacheInterceptor.setKeyGenerator(keyGenerator);
            return keyGenerator;
        }
    }
}
