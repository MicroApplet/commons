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
import com.asialjim.microapplet.common.utils.JsonUtil;
import lombok.Setter;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Optional;


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
    private final RedisConnectionFactory connectionFactory;
    private final GenericJacksonJsonRedisSerializer redisSerializer;
    private final CacheNameAndTTLHub cacheNameAndTTLHub;

    public CacheConfig(RedisConnectionFactory connectionFactory,
                       CacheNameAndTTLHub cacheNameAndTTLHub,
                       @SuppressWarnings("NullableProblems") @Nullable ObjectProvider<GenericJacksonJsonRedisSerializer> redisSerializerObjectProvider) {

        this.connectionFactory = connectionFactory;
        this.redisSerializer = Optional.ofNullable(redisSerializerObjectProvider)
                .map(ObjectProvider::getIfAvailable)
                .orElseGet(() -> GenericJacksonJsonRedisSerializer.builder().customize(JsonUtil::init).build());
        this.cacheNameAndTTLHub = cacheNameAndTTLHub;
    }

    @Bean
    @ConditionalOnBean
    public RedisCacheManager cacheManager() {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer.UTF_8))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

        return new DynamicTTLRedisCacheManager(
                RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory),
                defaultConfig, cacheNameAndTTLHub);
    }

    @Bean
    @ConditionalOnBean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory,
                                                       GenericJacksonJsonRedisSerializer jsonSerializer) {

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