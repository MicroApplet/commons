/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.cache.redis.config;

import com.asialjim.microapplet.common.cache.CacheNameAndTTLHub;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;

import java.time.Duration;
import java.util.Objects;

/**
 * 动态过期时间缓存管理器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class DynamicTTLRedisCacheManager extends RedisCacheManager {

    private final RedisCacheWriter cacheWriter;
    private final CacheNameAndTTLHub cacheNameAndTTLHub;
    private final RedisCacheConfiguration defaultCacheConfiguration;

    public DynamicTTLRedisCacheManager(RedisCacheWriter cacheWriter,
                                       RedisCacheConfiguration defaultCacheConfiguration,
                                       CacheNameAndTTLHub cacheNameAndTTLHub) {
        super(cacheWriter, defaultCacheConfiguration);
        this.cacheWriter = cacheWriter;
        this.defaultCacheConfiguration = defaultCacheConfiguration;
        this.cacheNameAndTTLHub = cacheNameAndTTLHub;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    protected RedisCache createRedisCache(String name, RedisCacheConfiguration config) {
        Duration nonEmptyTTL = cacheNameAndTTLHub.nonNullTTL(name);
        Duration emptyTTL = cacheNameAndTTLHub.nullTTL(name);
        if (Objects.isNull(config))
            config = defaultCacheConfiguration;

        // 合并配置并创建自定义 RedisCache
        RedisCacheConfiguration customConfig = config.entryTtl(nonEmptyTTL);
        return new DynamicTTLRedisCache(name, cacheWriter, customConfig, nonEmptyTTL, emptyTTL);
    }
}