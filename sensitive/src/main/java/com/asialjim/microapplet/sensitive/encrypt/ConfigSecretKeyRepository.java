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

package com.asialjim.microapplet.sensitive.encrypt;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.NoSuchAlgorithmException;

/**
 * 默认密钥仓库 — 基于 {@link SensitiveEncryptProperties} 配置读取 Base64 密钥
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Component
@RequiredArgsConstructor
public class ConfigSecretKeyRepository implements SecretKeyRepository {

    private final SensitiveEncryptProperties properties;

    @Override
    public Pair pairOf(AlgorithmMode mode) throws NoSuchAlgorithmException {
        if (properties.getKeys() == null)
            throw new NoSuchAlgorithmException("未配置敏感数据加密密钥: sensitive.encrypt.keys");

        return properties.getKeys().stream()
                .filter(p -> mode.getCode().equals(p.getMode()))
                .findFirst()
                .map(SensitiveEncryptKeyProperty::pair)
                .orElseThrow(() -> new NoSuchAlgorithmException(
                        "未找到算法模式 [" + mode.getCode() + "] 的密钥配置"));
    }
}
