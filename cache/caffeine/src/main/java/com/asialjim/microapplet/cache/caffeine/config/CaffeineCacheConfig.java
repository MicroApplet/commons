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

package com.asialjim.microapplet.cache.caffeine.config;

import com.asialjim.microapplet.cache.caffeine.manager.CaffeineCacheManagerWithCacheStrategy;
import com.asialjim.microapplet.cache.core.manager.L1Manager;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyHub;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Caffeine 缓存配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
@EnableCaching
public class CaffeineCacheConfig {
    @Bean
    public L1Manager caffeineCacheManager(CacheStrategyHub hub){
        return new CaffeineCacheManagerWithCacheStrategy(hub);
    }
}