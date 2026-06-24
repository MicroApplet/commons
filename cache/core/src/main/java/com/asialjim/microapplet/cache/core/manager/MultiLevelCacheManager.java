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

package com.asialjim.microapplet.cache.core.manager;

import com.asialjim.microapplet.cache.core.strategy.CacheStrategyHub;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("NullableProblems")
public record MultiLevelCacheManager(
        ConcurrentHashMap<String, Cache> cacheMap,
        CacheStrategyHub cacheStrategyHub,
        CacheManager l1manager,
        CacheManager l2manager
) implements CacheManager {

    @Override
    public Cache getCache(String name) {
        if (StringUtils.isBlank(name))
            throw new IllegalArgumentException("缓存名称不能为空");

        return cacheMap.computeIfAbsent(
                name,
                k -> {
                    try {
                        return new MultiLevelCache(cacheStrategyHub.nameOf(k),
                                l1manager.getCache(k),
                                l2manager.getCache(k)
                        );
                    } catch (Throwable t) {
                        throw new IllegalStateException("初始化缓存实力实例失败，name=" + k, t);
                    }
                }
        );
    }

    @Override
    public Collection<String> getCacheNames() {
        return cacheStrategyHub.names();
    }
}