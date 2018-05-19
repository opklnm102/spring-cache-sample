package me.dong.bootgeneratebeancustom.support.cache;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ethan.kim on 2018. 5. 20..
 */
@ConfigurationProperties(prefix = "spring.cache.redis")
public class CacheRedisProperties {

    private long defaultExpiration = 0L;

    private Map<String, Long> expiration = new HashMap<>();

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public long getDefaultExpiration() {
        return defaultExpiration;
    }

    public Map<String, Long> getExpiration() {
        return expiration;
    }

    public void setExpiration(Map<String, Long> expiration) {
        this.expiration = expiration;
    }
}
