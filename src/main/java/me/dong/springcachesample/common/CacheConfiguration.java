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
import java.util.stream.Stream;

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

        @Value("${resource.redis.host}")
        private String redisHostName;

        @Value("${resource.redis.port}")
        private int redisPort;

        /**
         */
        public class JsonRedisTemplate<V> extends RedisTemplate<String, V> {

            public JsonRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper, Class valueType) {
                RedisSerializer<String> stringSerializer = new StringRedisSerializer();
                setKeySerializer(stringSerializer);
                setHashKeySerializer(stringSerializer);
                setHashValueSerializer(stringSerializer);
                Jackson2JsonRedisSerializer jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(valueType);
                jsonRedisSerializer.setObjectMapper(objectMapper);
                setValueSerializer(jsonRedisSerializer);
                setConnectionFactory(connectionFactory);
                afterPropertiesSet();
            }

        }


        @Bean
        public CacheManagerCustomizer<RedisCacheManager> cacheManagerCustomizer() {
            return new CacheManagerCustomizer<RedisCacheManager>() {
                @Override
                public void customize(RedisCacheManager cacheManager) {
                    cacheManager.setDefaultExpiration(cacheRedisProperties.getDefaultExpireTime());
                    cacheManager.setExpires(cacheRedisProperties.getExpireTime());

                }

            };
        }

        private RedisCacheConfiguration createRedisCacheConfiguration(long ttl) {
            return RedisCacheConfiguration.defaultCacheConfig()
                    .entryTtl(Duration.ofSeconds(ttl));
        }


        @Bean
        public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                              ObjectMapper objectMapper) {

            Map<String, RedisCacheConfiguration> redisCacheConfigurations = new HashMap<>();
            redisCacheConfigurations.put(BookService.CACHE_NAME, createRedisCacheConfiguration(cacheBookTimeToLive));
            redisCacheConfigurations.put(CategoryService.CACHE_NAME,  createRedisCacheConfiguration(cacheCategoryTimeToLive));

            RedisCacheManager cacheManager = RedisCacheManager.builder(redisConnectionFactory)
                    .withInitialCacheConfigurations(redisCacheConfigurations)
                    .build();





            // Use the default redisTemplate for caching REST calls
//            cacheManager.withCache(CachedRestClient.CACHE_NAME, this.cacheNetworkTimeToLive);


            // Use
            JsonRedisTemplate blogTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    BlogService.CACHE_TYPE);
            cacheManager.withCache(BlogService.CACHE_NAME, blogTemplate, this.cacheBlogTimeToLive);

            JsonRedisTemplate teamTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    TeamService.CACHE_TYPE);
            cacheManager.withCache(TeamService.CACHE_NAME, teamTemplate, this.cacheTeamTimeToLive);

            JsonRedisTemplate guidesTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    GettingStartedGuides.CACHE_TYPE);
            cacheManager.withCache(GettingStartedGuides.CACHE_NAME, guidesTemplate, this.cacheGuideTimeToLive);

            JsonRedisTemplate understandingTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    UnderstandingDocs.CACHE_TYPE);
            cacheManager.withCache(UnderstandingDocs.CACHE_NAME, understandingTemplate, this.cacheUnderstandingTimeToLive);

            JsonRedisTemplate tutorialTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    Tutorials.CACHE_TYPE);
            cacheManager.withCache(Tutorials.CACHE_NAME, tutorialTemplate, this.cacheTutorialTimeToLive);

            JsonRedisTemplate topicalTemplate = new JsonRedisTemplate<>(redisConnectionFactory, objectMapper,
                    Topicals.CACHE_TYPE);
            cacheManager.withCache(Topicals.CACHE_NAME, topicalTemplate, this.cacheTopicalTimeToLive);

            return cacheManager;
        }

        @Bean
        public RedisConnectionFactory redisConnectionFactory() {
            return connectionFactory().redisConnectionFactory();
        }

        @Bean
        public JedisConnectionFactory jedisConnectionFactory(RedisProperties redisProperties) {
            JedisConnectionFactory factory = new JedisConnectionFactory();
            factory.setHostName(redisProperties.getHost());
            factory.setPort(redisProperties.getPort());
            factory.setUsePool(true);
            return factory;
        }


        @Bean
        public StringRedisSerializer stringRedisSerializer() {
            return new StringRedisSerializer();
        }


        @Bean
        public RedisTemplate<String, Object> redisTemplate() {
            final RedisTemplate<String, Object> template = new RedisTemplate<>();
            template.setConnectionFactory(jedisConnectionFactory());
            template.setKeySerializer(new StringRedisSerializer());
            template.setHashKeySerializer(new StringRedisSerializer());
            template.setHashValueSerializer(new GenericToStringSerializer<>(Object.class));
            template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
            return template;
        }

//        @Bean
//        public RedisCacheManager cacheManager() {
//            RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate());
//            return cacheManager;
//        }

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
