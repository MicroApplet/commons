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

package com.asialjim.microapplet.session;

import com.asialjim.microapplet.cache.core.strategy.CacheStrategy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

/**
 * 会话缓存策略
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Getter
@RequiredArgsConstructor
public enum SessionCacheStrategy implements CacheStrategy {
    /**
     * 用户令牌 → 会话
     */
    USER_SESSION_BY_TOKEN(SessionCache.sessionByToken, false, Duration.ZERO, Duration.ofMinutes(30), Duration.ZERO);

    private final String name;
    private final boolean localAvailable;
    private final Duration localTTL;
    private final Duration TTL;
    private final Duration nullTTL;
}
