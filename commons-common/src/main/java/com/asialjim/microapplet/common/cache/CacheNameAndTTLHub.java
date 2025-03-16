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

package com.asialjim.microapplet.common.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缓存名与过期时间
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class CacheNameAndTTLHub {
    private static final Map<String, Duration> nonNullTTLMap = new HashMap<>();
    private static final Map<String, Duration> nullTTLMap = new HashMap<>();

    public CacheNameAndTTLHub(@Autowired(required = false) List<CacheNameAndTTL> list) {
        if (CollectionUtils.isEmpty(list))
            return;
        for (CacheNameAndTTL item : list) {
            nonNullTTLMap.put(item.getName(),item.getNonNullTTL());
            nullTTLMap.put(item.getName(),item.getNullTTL());
        }
    }

    public Duration nonNullTTL(String name){
        return nonNullTTLMap.getOrDefault(name, Duration.ofMinutes(30));
    }

    public Duration nullTTL(String name){
        return nullTTLMap.getOrDefault(name, Duration.ofMinutes(5));
    }
}