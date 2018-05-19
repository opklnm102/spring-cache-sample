package me.dong.springboot1rediscache.support.cache;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by ethan.kim on 2018. 5. 20..
 */
public class JsonRedisTemplate<V> extends RedisTemplate<String, V> {

    public JsonRedisTemplate(RedisConnectionFactory connectionFactory, ObjectMapper objectMapper, Class<V> valueType) {
        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
        setKeySerializer(stringSerializer);
        setHashKeySerializer(stringSerializer);

        // TODO: 2018. 5. 20. hash value는 왜 string??
        setHashValueSerializer(stringSerializer);

        Jackson2JsonRedisSerializer<V> jsonRedisSerializer = new Jackson2JsonRedisSerializer<>(valueType);
        jsonRedisSerializer.setObjectMapper(objectMapper);
        setValueSerializer(jsonRedisSerializer);
        setConnectionFactory(connectionFactory);
        afterPropertiesSet();
    }

}
