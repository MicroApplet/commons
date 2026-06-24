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

import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import org.springframework.cache.Cache;

import java.time.Duration;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;

/**
 * 二级缓存
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
@SuppressWarnings("NullableProblems")
public record MultiLevelCache(
        CacheStrategy strategy,
        Cache l1cache,
        Cache l2cache
) implements Cache {

    @Override
    public String getName() {
        return strategy.getName();
    }

    @Override
    public Object getNativeCache() {
        // 通常返回底层缓存，如Redis
        return l2cache.getNativeCache();
    }

    private boolean nullable(){
        Duration nullTTL = strategy.getNullTTL();
        if (Objects.isNull(nullTTL))
            return false;

        return nullTTL.compareTo(Duration.ZERO) > 0;
    }

    @Override
    public ValueWrapper get(Object key) {
        boolean localAvailable = strategy.isLocalAvailable();
        boolean nullValueCacheable = nullable();

        ValueWrapper value;

        if (localAvailable){
            value = l1cache.get(key);

            if (Objects.nonNull(value))
                if (Objects.nonNull(value.get()) || nullValueCacheable)
                    return value;
        }

        value = l2cache.get(key);
        if (localAvailable){
            if (Objects.nonNull(value)){
                if (Objects.nonNull(value.get()))
                    l1cache.put(key,value.get());
            } else {
                if (nullValueCacheable)
                    l1cache.put(key,null);
            }
        }

        if (Objects.nonNull(value) && Objects.nonNull(value.get())){
            if (localAvailable)
                l1cache.put(key,value.get());
        }

        return value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        boolean localAvailable = strategy.isLocalAvailable();
        boolean nullValueCacheable = this.nullable();

        if (localAvailable){
            T t = l1cache.get(key,type);

            if (Objects.nonNull(t))
                return t;

            if (nullValueCacheable)
                return null;

            t = l2cache.get(key,type);
            if (Objects.nonNull(t))
                l1cache.put(key,t);
            return t;
        }

        return l2cache.get(key,type);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        boolean localAvailable = strategy.isLocalAvailable();

        T value;
        if (localAvailable)
            value = l1cache.get(key,() -> l2cache.get(key,valueLoader));
        else
            value = l2cache.get(key,valueLoader);
        put(key,value);
        return value;
    }

    @Override
    public void put(Object key, Object value) {
        boolean localAvailable = strategy.isLocalAvailable();
        boolean nullValueCacheable = nullable();

        if (Objects.nonNull(value)) {
            if (localAvailable)
                l1cache.put(key, value);
            l2cache.put(key,value);
            return;
        }

        if (!nullValueCacheable)
            return;

        if (localAvailable)
            l1cache.put(key,null);
        l2cache.put(key,null);
    }

    @Override
    public void evict(Object key) {
        l1cache.evict(key);
        l2cache.evict(key);
    }

    @Override
    public void clear() {
        l1cache.clear();
        l2cache.clear();
    }
}