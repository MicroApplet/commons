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

package com.asialjim.microapplet.cache.core.strategy;

import org.apache.commons.lang3.Strings;

import java.time.Duration;
import java.util.*;
import java.util.function.Supplier;

/**
 * 缓存名与过期时间
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class CacheStrategyHub {
    private final Map<String,CacheStrategy> map;
    private final Map<String,CacheStrategy> localMap;
    private final CacheStrategyRepository repository;
    private final Set<CacheStrategy> strategies;

    public CacheStrategyHub(Set<CacheStrategy> strategies, CacheStrategyRepository repository) {
          this.strategies = strategies;
          this.repository = repository;
          this.map = new HashMap<>();
          this.localMap = new HashMap<>();

          if (Objects.nonNull(strategies) && !strategies.isEmpty())
              strategies.forEach(strategy ->  localMap.put(strategy.getName(),strategy));
    }


    private CacheStrategy local(String name){
        return localMap.computeIfAbsent(
                name,
                k -> strategies.stream()
                        .filter(item -> Strings.CI.equals(item.getName(), k))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("找不到" + name + "本地缓存策略配置"))
        );
    }

    public Collection<String> names(){
        return this.localMap.keySet();
    }

    public CacheStrategy nameOf(String name){
        return map.computeIfAbsent(
                name,
                k -> new CacheStrategyWrapper(
                        k,
                        local(k),
                        () -> repository.findByName(k)
                )
        );
    }


    private class CacheStrategyWrapper implements CacheStrategy{
        private final String name;
        private final CacheStrategy local;
        private final Supplier<Optional< CacheStrategy>> remote;

        private CacheStrategyWrapper(String name, CacheStrategy local, Supplier<Optional< CacheStrategy>>  remote) {
            this.name = name;
            if (Objects.isNull(local))
                throw new IllegalStateException("缓存策略" + name + "本地配置为空");
            this.local = local;
            if (Objects.isNull(remote))
                throw new IllegalStateException("缓存策略" + name + "未配置远端策略Supplier");
            this.remote = remote;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public boolean isLocalAvailable() {
            return remote.get().map(CacheStrategy::isLocalAvailable).orElseGet(local::isLocalAvailable);
        }

        @Override
        public Duration getLocalTTL() {
            return remote.get().map(CacheStrategy::getLocalTTL).orElseGet(local::getLocalTTL);
        }

        @Override
        public Duration getTTL() {
            return remote.get().map(CacheStrategy::getTTL).orElseGet(local::getLocalTTL);
        }

        @Override
        public Duration getNullTTL() {
            return remote.get().map(CacheStrategy::getNullTTL).orElseGet(local::getLocalTTL);
        }
    }
}