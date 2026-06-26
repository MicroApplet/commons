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

package com.asialjim.microapplet.spring.session;

import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import com.asialjim.microapplet.session.Session;
import com.asialjim.microapplet.session.SessionCacheStrategy;
import com.asialjim.microapplet.session.SessionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * 基于 Redis 的会话仓储
 * <p>Gateway 与下游业务服务使用相同的 key 前缀和序列化方式，确保令牌与会话数据一致。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@RequiredArgsConstructor
public class RedisSessionRepository implements SessionRepository {
    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 根据令牌查找会话
     */
    public Session findByToken(String token) {
        if (token == null || token.isBlank())
            return null;

        String key = SessionRepository.key(token);
        String json = stringRedisTemplate.opsForValue().get(key);
        if (StringUtils.isBlank(json))
            return null;

        return JsonUtil.instance.toBean(json, Session.class);
    }

    /**
     * 保存会话
     */
    public void save(Session session) {
        if (Objects.isNull(session) || session.getToken() == null || session.getToken().isBlank())
            return;

        String key = SessionRepository.key(session.getToken());

        String json = JsonUtil.instance.toStr(session);
        if (StringUtils.isBlank(json))
            return;

        stringRedisTemplate.opsForValue().set(key,json, SessionCacheStrategy.USER_SESSION_BY_TOKEN.getTTL());
    }

    /**
     * 删除会话
     */
    public void delete(String token) {
        if (token == null || token.isBlank())
            return;
        String key = SessionRepository.key(token);
        stringRedisTemplate.delete(key);
    }

    @Override
    public Mono<Session> findByTokenMono(String token) {
        throw new UnsupportedOperationException("Servlet环境不支持响应式操作");
    }

    @Override
    public Mono<Void> saveMono(Session session) {
        throw new UnsupportedOperationException("Servlet环境不支持响应式操作");
    }

    @Override
    public Mono<Void> deleteMono(String token) {
        throw new UnsupportedOperationException("Servlet环境不支持响应式操作");
    }

}
