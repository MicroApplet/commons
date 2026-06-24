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

import java.time.Duration;


/**
 * 缓存名与过期时间
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface CacheStrategy {
    /**
     * 缓存名
     */
    String getName();

    /**
     * 允许本地缓存
     */
    boolean isLocalAvailable();

    /**
     * 本地缓存时间
     */
    Duration getLocalTTL();

    /**
     * 不为空值缓存时间
     */
    Duration getTTL();

    /**
     * 空值缓存时间
     */
    Duration getNullTTL();
}