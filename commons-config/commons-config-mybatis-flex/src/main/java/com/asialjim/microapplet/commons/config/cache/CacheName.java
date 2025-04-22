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

package com.asialjim.microapplet.commons.config.cache;

import com.asialjim.microapplet.common.cache.CacheNameAndTTL;
import com.asialjim.microapplet.common.cache.CacheNameAndTTLConfig;
import lombok.Getter;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @since 2025 03 25, &nbsp;&nbsp; <em>version:</em>
 */
@Configuration
public class CacheName extends CacheNameAndTTLConfig {
    public interface Name {
        String CONF_CACHE = "_conf:type:biz:code:env";
    }

    @Getter
    enum Cache implements CacheNameAndTTL {
        PLACE_HOLDER("placeholder"),
        CONF_CACHE(Name.CONF_CACHE, Duration.ofHours(8), Duration.ofMinutes(1));

        Cache(String name) {
            this(name, Duration.ofHours(1));
        }

        Cache(String name, Duration nonNullTTL) {
            this(name, nonNullTTL, Duration.ofMinutes(1));
        }

        Cache(String name, Duration nonNullTTL, Duration nullTTL) {
            this.name = name;
            this.nonNullTTL = nonNullTTL;
            this.nullTTL = nullTTL;
        }

        private final String name;
        private final Duration nonNullTTL;
        private final Duration nullTTL;
    }


    @Override
    protected List<CacheNameAndTTL> list() {
        return Arrays.asList(Cache.values());
    }
}