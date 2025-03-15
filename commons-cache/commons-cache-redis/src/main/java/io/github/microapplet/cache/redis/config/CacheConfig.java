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

package io.github.microapplet.cache.redis.config;

import io.github.microapplet.common.cache.CacheNameAndTTLHub;
import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.CollectionUtils;

import java.util.Collections;


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
public class CacheConfig extends CachingConfigurerSupport {
    private static final GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

    @Bean
    @ConditionalOnMissingBean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        return cacheManager(connectionFactory, new CacheNameAndTTLHub(Collections.emptyList()));
    }

    @Bean
    @ConditionalOnBean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory, CacheNameAndTTLHub cacheNameAndTTLHub) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(jsonSerializer));

        return new DynamicTTLRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                defaultConfig, cacheNameAndTTLHub);
    }

    @Bean
    @ConditionalOnBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> res = new RedisTemplate<>();
        res.setKeySerializer(StringRedisSerializer.UTF_8);
        res.setValueSerializer(jsonSerializer);
        res.setHashKeySerializer(jsonSerializer);
        res.setHashValueSerializer(jsonSerializer);
        res.setDefaultSerializer(jsonSerializer);
        res.setEnableDefaultSerializer(true);
        res.setConnectionFactory(connectionFactory);
        res.afterPropertiesSet();
        return res;
    }
}