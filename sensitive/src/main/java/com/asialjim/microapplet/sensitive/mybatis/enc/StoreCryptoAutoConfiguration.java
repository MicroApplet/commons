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

package com.asialjim.microapplet.sensitive.mybatis.enc;

import com.asialjim.microapplet.sensitive.encrypt.AlgorithmModeConfig;
import com.asialjim.microapplet.sensitive.encrypt.SecretKeyRepository;
import com.asialjim.microapplet.sensitive.encrypt.SensitiveEncryptProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 存储加密体系自动配置。
 *
 * <p>装配 {@link StoreCipher} 及其依赖，复用现有 {@link SecretKeyRepository} 与 {@link AlgorithmModeConfig}。
 * 本配置仅注册存储加密所需 Bean，不改动现有传输/日志方案与 MyBatis 代理。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Configuration
public class StoreCryptoAutoConfiguration {

    @Bean
    public StoreKeyRepository storeKeyRepository(SecretKeyRepository secretKeyRepository) {
        return new StoreKeyRepository(secretKeyRepository);
    }

    @Bean
    public StoreEncryptor storeEncryptor(StoreKeyRepository storeKeyRepository) {
        return new StoreEncryptor(storeKeyRepository);
    }

    @Bean
    public BlindIndexTokenizer blindIndexTokenizer(StoreKeyRepository storeKeyRepository,
                                                   SensitiveEncryptProperties sensitiveEncryptProperties) {
        return new BlindIndexTokenizer(storeKeyRepository,
                sensitiveEncryptProperties.getBlindIndexGranularity(),
                sensitiveEncryptProperties.getTokenLength());
    }

    @Bean
    public StoreCipher storeCipher(AlgorithmModeConfig algorithmModeConfig,
                                   StoreEncryptor storeEncryptor,
                                   BlindIndexTokenizer blindIndexTokenizer) {
        return new StoreCipher(algorithmModeConfig, storeEncryptor, blindIndexTokenizer);
    }
}
