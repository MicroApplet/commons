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

import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.util.ByteUtils;

import java.time.Duration;

/**
 * 动态缓存过期时间Redis缓存器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class DynamicTTLRedisCache extends RedisCache {
    private static final byte[] BINARY_NULL_VALUE = RedisSerializer.java().serialize(NullValue.INSTANCE);

    private final Duration nonEmptyTTL;
    private final Duration emptyTTL;
    private final String name;
    private final RedisCacheConfiguration configuration;

    protected DynamicTTLRedisCache(String name,
                                   RedisCacheWriter cacheWriter,
                                   RedisCacheConfiguration configuration,
                                   Duration nonEmptyTTL,
                                   Duration emptyTTL) {

        super(name, cacheWriter, configuration);
        this.configuration = configuration;
        this.name = name;
        this.nonEmptyTTL = nonEmptyTTL;
        this.emptyTTL = emptyTTL;
    }

    @Override
    public void put(@SuppressWarnings("NullableProblems") Object key, Object value) {
        // 根据值是否为空选择 TTL
        Duration ttl = (value == null) ? emptyTTL : nonEmptyTTL;
        byte[] keyBytes = this.serializeCacheKey(this.createCacheKey(key));

        //noinspection DataFlowIssue
        byte[] valueBytes = (this.isAllowNullValues() && (value instanceof NullValue))
                ? BINARY_NULL_VALUE
                : ByteUtils.getBytes(this.configuration.getValueSerializationPair().write(value));

        // 自定义写入逻辑
        //noinspection DataFlowIssue
        getNativeCache().put(name, keyBytes, valueBytes, ttl);
    }
}