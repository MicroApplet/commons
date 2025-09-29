/*
 * Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.asialjim.microapplet.cache.redis.config;

import com.asialjim.microapplet.common.cache.CacheNameAndTTLHub;
import com.asialjim.microapplet.common.utils.JacksonUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import lombok.Setter;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.function.Consumer;


/**
 * 启用缓存
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Setter
@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return JacksonUtil.init(new JsonMapper());
    }

    @Bean
    @Primary
    public GenericJackson2JsonRedisSerializer jsonSerializer() {

        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer();
        json.configure(JacksonUtil::init);
        return json;
       /*
        ObjectMapper mapper = JacksonUtil.init(new ObjectMapper());
        return new GenericJsonRedisSerializer(mapper, "@class");
        */
    }


    @Bean
    @Primary
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory,
                                          CacheNameAndTTLHub cacheNameAndTTLHub,
                                          RedisTemplate<String, Object> redisTemplate,
                                          GenericJackson2JsonRedisSerializer jsonSerializer) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()));
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));

        return new DynamicTTLRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                defaultConfig, cacheNameAndTTLHub);
    }

    @Bean
    @Primary
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                       GenericJackson2JsonRedisSerializer jsonSerializer) {

       /*
        GenericJackson2JsonRedisSerializer json = new GenericJackson2JsonRedisSerializer();
        json.configure(JacksonUtil::init);
        */

        RedisTemplate<String, Object> res = new RedisTemplate<>();
        res.setKeySerializer(StringRedisSerializer.UTF_8);
//        res.setValueSerializer(json);
        res.setValueSerializer(jsonSerializer);
        res.setHashKeySerializer(jsonSerializer);
//        res.setHashValueSerializer(json);
        res.setHashValueSerializer(jsonSerializer);
        res.setDefaultSerializer(jsonSerializer);
        res.setEnableDefaultSerializer(true);
        res.setConnectionFactory(connectionFactory);
        res.afterPropertiesSet();
        return res;
    }
}