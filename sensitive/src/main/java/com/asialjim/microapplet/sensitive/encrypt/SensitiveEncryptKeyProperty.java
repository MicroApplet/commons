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

import lombok.Data;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * 敏感数据加密密钥配置项
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Data
public class SensitiveEncryptKeyProperty {
    /** 算法模式：GM / MODERN */
    private String mode;
    /** 加密密钥（Base64 编码） */
    private String encKey;
    /** MAC 签名密钥（Base64 编码，GM模式需要） */
    private String macKey;

    public SecretKeyRepository.Pair pair() {
        AlgorithmMode algorithmMode = AlgorithmMode.fromCode(mode);
        SecretKeyRepository.Pair pair = new SecretKeyRepository.Pair();

        if (encKey != null && !encKey.isBlank()) {
            byte[] keyBytes = Base64.getDecoder().decode(encKey);
            pair.setEncKey(new SecretKeySpec(keyBytes, algorithmMode.getEncAlgorithm()));
        }
        if (macKey != null && !macKey.isBlank()) {
            byte[] keyBytes = Base64.getDecoder().decode(macKey);
            pair.setMacKey(new SecretKeySpec(keyBytes, algorithmMode.getMacAlgorithm()));
        }
        return pair;
    }
}
