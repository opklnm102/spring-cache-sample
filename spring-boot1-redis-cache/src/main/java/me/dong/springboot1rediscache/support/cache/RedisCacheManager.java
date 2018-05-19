package me.dong.springboot1rediscache.support.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.data.redis.cache.DefaultRedisCachePrefix;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCachePrefix;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.Assert;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * refrence by spring.io sagan project
 * https://github.com/spring-io/sagan
 * <p>
 * Created by ethan.kim on 2018. 5. 20..
 */
public class RedisCacheManager extends AbstractTransactionSupportingCacheManager {

    private final RedisConnectionFactory connectionFactory;

    private final RedisTemplate<?, ?> defaultTemplate;

    private final Map<String, RedisTemplate<?, ?>> templates;

    private boolean usePrefix = true;

    private RedisCachePrefix cachePrefix = new DefaultRedisCachePrefix();

    private boolean dynamic = false;

    // 0 - never expire
    private long defaultExpiration = 0;

    private final Map<String, Long> expires;

    public RedisCacheManager(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
        this.defaultTemplate = new RedisTemplate<>();
        this.defaultTemplate.setConnectionFactory(connectionFactory);
        this.defaultTemplate.afterPropertiesSet();
        this.templates = new ConcurrentHashMap<>();
        this.expires = new ConcurrentHashMap<>();
    }

    public void setUsePrefix(boolean usePrefix) {
        this.usePrefix = usePrefix;
    }

    public void setCachePrefix(RedisCachePrefix cachePrefix) {
        this.cachePrefix = cachePrefix;
    }

    public void setDynamic(boolean dynamic) {
        this.dynamic = dynamic;
    }

    public void setDefaultExpiration(long defaultExpiration) {
        this.defaultExpiration = defaultExpiration;
    }

    public RedisCacheManager withCache(String cacheName, long expiration) {
        return withCache(cacheName, this.defaultTemplate, expiration);
    }

    public RedisCacheManager withCache(String cacheName, RedisTemplate<?, ?> template, long expiration) {
        this.templates.put(cacheName, template);
        this.expires.put(cacheName, expiration);
        RedisCache cache = createCache(cacheName, template, expiration);
        addCache(cache);
        return this;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = super.getCache(name);
        if (cache == null && this.dynamic) {
            return createCache(name, this.defaultTemplate, this.defaultExpiration);
        }
        return cache;
    }

    protected RedisCache createCache(String cacheName, RedisTemplate<?, ?> template, long expiration) {
        return new RedisCache(cacheName, (usePrefix ? cachePrefix.prefix(cacheName) : null), template, expiration);
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        Assert.notNull(this.defaultTemplate, "A redis template is required in order to interact with data store");
        return this.getCacheNames().stream()
                .map(this::getCache)
                .collect(Collectors.toList());
    }
}
