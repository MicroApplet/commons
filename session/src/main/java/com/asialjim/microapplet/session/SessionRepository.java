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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

/**
 * 会话仓储
 * <p>定义会话的存取接口，由不同基础设施模块实现。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public interface SessionRepository {
    String bean = "SessionRepository";

    /** Redis key 分隔符 — 与 Spring Cache 默认分隔符一致 */
    String KEY_SEPARATOR = "::";

    /**
     * 根据令牌查找会话
     */
    Session findByToken(String token);

    /**
     * 保存会话
     */
    void save(Session session);

    /**
     * 删除会话
     */
    void delete(String token);

    /**
     * 根据令牌查找会话（响应式）
     */
    Mono<Session> findByTokenMono(String token);

    /**
     * 保存会话（响应式）
     */
    Mono<Void> saveMono(Session session);

    /**
     * 删除会话（响应式）
     */
    Mono<Void> deleteMono(String token);

    /**
     * 构造 Redis key：{cacheName}::{token}
     */
    static String key(String token) {
        return SessionCache.sessionByToken + KEY_SEPARATOR + token;
    }

    @Configuration
    @ConditionalOnMissingBean(SessionRepository.class)
    class UnSupport implements SessionRepository{

        @Override
        public Session findByToken(String token) {
            throw new IllegalStateException("未实现上下文");
        }

        @Override
        public void save(Session session) {
            throw new IllegalStateException("未实现上下文");

        }

        @Override
        public void delete(String token) {
            throw new IllegalStateException("未实现上下文");

        }

        @Override
        public Mono<Session> findByTokenMono(String token) {
            throw new IllegalStateException("未实现上下文");
        }

        @Override
        public Mono<Void> saveMono(Session session) {
            throw new IllegalStateException("未实现上下文");
        }

        @Override
        public Mono<Void> deleteMono(String token) {
            throw new IllegalStateException("未实现上下文");
        }
    }
}
