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

package io.github.microapplet.common.cache;

import org.springframework.context.annotation.Bean;

import java.util.Collections;
import java.util.List;
import java.util.Optional;


/**
 * 缓存名与过期时间
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public abstract class CacheNameAndTTLConfig {

    @Bean
    public List<CacheNameAndTTL> cacheNameAndTTLList(){
        return Optional.ofNullable(list()).orElseGet(Collections::emptyList);
    }

    protected abstract List<CacheNameAndTTL> list();
}