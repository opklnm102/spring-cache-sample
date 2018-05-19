package me.dong.bootgeneratebeancustom.common.config;

import org.springframework.boot.autoconfigure.cache.CacheManagerCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;

import me.dong.bootgeneratebeancustom.support.cache.CacheRedisProperties;

/**
 * @see org.springframework.boot.autoconfigure.cache.RedisCacheConfiguration
 * refrence by http://javacan.tistory.com/entry/customize-redis-cache-expire-time-in-boot
 * <p>
 * Created by ethan.kim on 2018. 5. 17..
 */
@Configuration
@EnableCaching  // (proxyTargetClass = true)
@EnableConfigurationProperties(value = CacheRedisProperties.class)
public class CacheConfiguration {

    private final CacheRedisProperties cacheRedisProperties;

    public CacheConfiguration(CacheRedisProperties cacheRedisProperties) {
        this.cacheRedisProperties = cacheRedisProperties;
    }

    // Customizer를 bean으로 등록하면 auto configuration 시점에 customizing을 한다
    // boot 2.0 부터는 RedisCacheConfiguration에 expiration을 설정할 수 있다
    // Customizer로 할 수 있는게 거의 없음... 있다면 tractionAware 설정정도...?
    @Bean
    public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
        return cacheManager -> {
            cacheManager.setDefaultExpiration(cacheRedisProperties.getDefaultExpiration());
            cacheManager.setExpires(cacheRedisProperties.getExpiration());
        };
    }
}
