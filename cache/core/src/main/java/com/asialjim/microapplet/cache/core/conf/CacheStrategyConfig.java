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

package com.asialjim.microapplet.cache.core.conf;

import com.asialjim.microapplet.cache.core.manager.HashMapCacheManager;
import com.asialjim.microapplet.cache.core.manager.L1Manager;
import com.asialjim.microapplet.cache.core.manager.L2Manager;
import com.asialjim.microapplet.cache.core.manager.MultiLevelCacheManager;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyCollection;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyHub;
import com.asialjim.microapplet.cache.core.strategy.CacheStrategyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Order
@Configuration
@EnableCaching
public class CacheStrategyConfig {
    private L1Manager l1Manager;
    private L2Manager l2Manager;

    @Autowired(required = false)
    public void setL1Manager(L1Manager l1Manager) {
        this.l1Manager = l1Manager;
    }

    @Autowired(required = false)
    public void setL2Manager(L2Manager l2Manager) {
        this.l2Manager = l2Manager;
    }


    @Bean
    @Order
    @ConditionalOnMissingBean(CacheStrategyRepository.class)
    public CacheStrategyRepository emptyRepository(){
        return name -> Optional.empty();
    }

    @Bean
    @Primary
    public CacheStrategyHub cacheStrategyHub(List<CacheStrategyCollection> collections,
                                             CacheStrategyRepository repository){
        final Set<String> names = new HashSet<>();
        final Set<CacheStrategy> strategies = new HashSet<>();

        for (CacheStrategyCollection collection : collections) {
            Set<CacheStrategy> set = collection.set();
            if (Objects.isNull(set) || set.isEmpty())
                continue;

            for (CacheStrategy strategy : set) {
                String name = strategy.getName();
                if (names.contains(name))
                    throw new IllegalStateException("重复的缓存策略名称：" + name + ", 请修改名称");

                names.add(name);
                strategies.add(
                        strategy
                );
            }
        }

        return new CacheStrategyHub(strategies,repository);
    }

    @Bean
    @Primary
    public CacheManager multiLevelCacheManager(CacheStrategyHub cacheStrategyHub){
        return new MultiLevelCacheManager(
                new ConcurrentHashMap<>(),
                cacheStrategyHub,
                Optional.ofNullable(l1Manager).orElseGet(() -> new HashMapCacheManager(new ConcurrentMapCacheManager())),
                Optional.ofNullable(l2Manager).orElseGet(() -> new HashMapCacheManager(new ConcurrentMapCacheManager()))
        );
    }
}