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

package com.asialjim.microapplet.cache.redis.manager;

import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import org.jspecify.annotations.Nullable;
import org.springframework.cache.support.NullValue;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.util.ByteUtils;

import java.time.Duration;
import java.util.Objects;

public class RedisCacheWithCacheStrategy extends RedisCache {
    private static final byte[] NULL_VALUE = RedisSerializer.java().serialize(NullValue.INSTANCE);

    private final CacheStrategy strategy;

    public RedisCacheWithCacheStrategy(RedisCacheWriter writer,
                                       RedisCacheConfiguration configuration,
                                       CacheStrategy strategy) {
        super(strategy.getName(), writer, configuration);
        this.strategy = strategy;
    }

    @Override
    @SuppressWarnings({"ConstantValue", "NullableProblems"})
    public void put(Object key, @Nullable Object value) {
        if (Objects.isNull(key))
            return;

        boolean nullValueCacheable = nullable();

        if (!nullValueCacheable && Objects.isNull(value))
            return;

        Duration ttl = strategy.getTTL();
        Duration nullTTL = strategy.getNullTTL();
        Duration target = Objects.nonNull(value)? ttl:nullTTL;
        byte[] keyBytes = serializeCacheKey(this.createCacheKey(key));
        byte[] valueBytes;

        if (Objects.isNull(value) || value instanceof NullValue)
            valueBytes = NULL_VALUE;
        else
            valueBytes = ByteUtils.getBytes(getCacheConfiguration().getValueSerializationPair().write(value));

        if (Objects.nonNull(target) && target.isPositive())
            getNativeCache().put(strategy.getName(),keyBytes,valueBytes,target);
    }

    private boolean nullable(){
        Duration nullTTL = strategy.getNullTTL();
        if (Objects.isNull(nullTTL))
            return false;

        return nullTTL.compareTo(Duration.ZERO) > 0;
    }
}
