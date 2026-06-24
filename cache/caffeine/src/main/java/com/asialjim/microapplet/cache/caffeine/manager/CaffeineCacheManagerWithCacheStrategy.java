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

package com.asialjim.microapplet.cache.caffeine.manager;

import com.asialjim.microapplet.cache.core.manager.L1Manager;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyHub;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCacheManager;

@SuppressWarnings("NullableProblems")
public class CaffeineCacheManagerWithCacheStrategy extends CaffeineCacheManager implements L1Manager {
    private final CacheStrategyHub hub;
    public CaffeineCacheManagerWithCacheStrategy(CacheStrategyHub hub) {
        this.hub = hub;
    }

    @Override
    protected Cache createCaffeineCache(String name) {
        CacheStrategy cacheStrategy = hub.nameOf(name);
        return new CaffeineCacheWithCacheStrategy(cacheStrategy);
    }
}