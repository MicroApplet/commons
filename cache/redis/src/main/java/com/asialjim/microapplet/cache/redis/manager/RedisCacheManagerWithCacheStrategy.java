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

import com.asialjim.microapplet.cache.core.manager.L2Manager;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyHub;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Optional;

public class RedisCacheManagerWithCacheStrategy extends RedisCacheManager implements L2Manager {
    private final CacheStrategyHub hub;

    public RedisCacheManagerWithCacheStrategy(RedisCacheWriter redisCacheWriter, RedisCacheConfiguration conf, CacheStrategyHub hub) {
        super(redisCacheWriter,conf);
        this.hub = hub;
    }

    @Override
    protected RedisCache createRedisCache(String name, @Nullable RedisCacheConfiguration cacheConfiguration) {
        CacheStrategy cacheStrategy = Optional.of(hub)
                .map(item -> item.nameOf(name))
                .orElseGet(() -> new CacheStrategy() {
                    @Override
                    public String getName() {
                        return name;
                    }

                    @Override
                    public boolean isLocalAvailable() {
                        return false;
                    }

                    @Override
                    public Duration getLocalTTL() {
                        return null;
                    }

                    @Override
                    public Duration getTTL() {
                        return Duration.ofMinutes(30);
                    }

                    @Override
                    public Duration getNullTTL() {
                        return Duration.ZERO;
                    }
                });

        return new RedisCacheWithCacheStrategy(
                this.getCacheWriter(),
                cacheConfiguration,
                cacheStrategy
        );
    }
}
