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

import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;

import java.time.Duration;
import java.util.Optional;

public class CaffeineCacheWithCacheStrategy extends CaffeineCache {
    public CaffeineCacheWithCacheStrategy( CacheStrategy strategy) {
        super(
                strategy.getName(),
                Caffeine.newBuilder()
                        .expireAfterWrite(Optional.of(strategy).map(CacheStrategy::getLocalTTL).orElse(Duration.ZERO))
                        .maximumSize(100)
                        .build(),
                Optional.of(strategy).map(CacheStrategy::getLocalTTL).map(item -> item.compareTo(Duration.ZERO) > 0)
                        .orElse(false)
        );
    }
}
