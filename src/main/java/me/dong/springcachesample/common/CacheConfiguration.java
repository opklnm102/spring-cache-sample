package me.dong.springcachesample.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.dong.springcachesample.book.BookService;
import me.dong.springcachesample.category.CategoryService;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@EnableCaching // (proxyTargetClass = true)
@Configuration
@Slf4j
public class CacheConfiguration {

    @Profile(SampleProfiles.CLOUD)
    static class CloudCacheConfiguration {

//        @Value(BookService.CACHE_TTL)
//        private Long cacheNetworkTimeToLive;

        @Value(BookService.CACHE_TTL)
        private Long cacheBookTimeToLive;

        @Value(CategoryService.CACHE_TTL)
        private Long cacheCategoryTimeToLive;

        @Bean
        public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
            Map<String, RedisCacheConfiguration> redisCacheConfigurations = new HashMap<>();
            redisCacheConfigurations.put(BookService.CACHE_NAME, createRedisCacheConfiguration(cacheBookTimeToLive));
            redisCacheConfigurations.put(CategoryService.CACHE_NAME, createRedisCacheConfiguration(cacheCategoryTimeToLive));
            // TODO: 2018. 5. 18. rest call cache
            // Use the default redisTemplate for caching REST calls
//            cacheManager.withCache(CachedRestClient.CACHE_NAME, this.cacheNetworkTimeToLive);

            return RedisCacheManager.builder(redisConnectionFactory)
                    .withInitialCacheConfigurations(redisCacheConfigurations)
                    .build();
        }

        private RedisCacheConfiguration createRedisCacheConfiguration(long ttl) {
            return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(ttl));
        }
    }

    @Profile(SampleProfiles.STANDALONE)
    static class LocalCacheConfiguration extends CachingConfigurerSupport {

        /**
         * Custom key generator
         *
         * @return
         */
        @Override
        public KeyGenerator keyGenerator() {
            return (target, method, params) -> {
                StringBuilder sb = new StringBuilder();

                sb.append(target.getClass().getName());
                sb.append(method.getName());

                for (Object param : params) {
                    sb.append(param);
                }

                log.info("{}", sb.toString());

                return sb.toString();
            };
        }
    }
}
