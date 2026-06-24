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
import com.asialjim.microapplet.session.MamsUserEncKeyRepository;
import com.asialjim.microapplet.session.MamsUserEncKeyResParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 基于 Redis 的会话仓储
 * <p>Gateway 与下游业务服务使用相同的 key 前缀和序列化方式，确保令牌与会话数据一致。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@RequiredArgsConstructor
@Component(MamsUserEncKeyRepository.bean)
@ConditionalOnClass(ReactiveStringRedisTemplate.class)
public class ReactiveRedisMamsUserEncKeyRepository implements MamsUserEncKeyRepository {
    private final ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    @Override
    public MamsUserEncKeyResParam get(String subAppTypeCode, String openid, String version) {
        throw new UnsupportedOperationException("Reactive环境不支持阻塞式操作");
       }

    @Override
    public void set(String subAppTypeCode, String openid, String version, MamsUserEncKeyResParam encryptKey) {
        throw new UnsupportedOperationException("Reactive环境不支持阻塞式操作");
    }

    @Override
    public Mono<MamsUserEncKeyResParam> getMono(String subAppTypeCode, String openid, String version) {
        String key = key(subAppTypeCode, openid, version);
        return reactiveStringRedisTemplate.opsForValue().get(key)
                .switchIfEmpty(Mono.empty())
                .map(item -> JsonUtil.instance.toBean(item,MamsUserEncKeyResParam.class));
    }

    @Override
    public Mono<Void> setMono(String subAppTypeCode, String openid, String version, MamsUserEncKeyResParam encryptKey) {
        String key = key(subAppTypeCode, openid, version);
        return reactiveStringRedisTemplate.opsForValue()
                .set(key, JsonUtil.instance.toStr(encryptKey), Duration.ofHours(6)).then();
    }
}
