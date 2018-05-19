package me.dong.springboot1rediscache.common.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import me.dong.springboot1rediscache.book.Book;
import me.dong.springboot1rediscache.book.BookService;
import me.dong.springboot1rediscache.support.cache.JsonRedisTemplate;
import me.dong.springboot1rediscache.support.cache.RedisCacheManager;

/**
 * Created by ethan.kim on 2018. 5. 17..
 */
@EnableCaching  // (proxyTargetClass = true)
@Configuration
@Slf4j
public class CacheConfiguration {

    @Value(BookService.CACHE_TTL)
    private Long cacheBookTimeToLive;

    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory connectionFactory,
                                          ObjectMapper objectMapper) {

        RedisCacheManager cacheManager = new RedisCacheManager(connectionFactory);

        JsonRedisTemplate<Book> bookTemplate = new JsonRedisTemplate<>(connectionFactory, objectMapper, BookService.CACHE_TYPE);
        cacheManager.withCache(BookService.CACHE_NAME, bookTemplate, this.cacheBookTimeToLive);

        return cacheManager;
    }
}
