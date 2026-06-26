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

import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

/**
 * 加密上下文
 */
public class EncryptionContext {
    private EncryptionStrategy strategy;

    public EncryptionContext() {
        this.strategy = EncryptionStrategyFactory.getStrategy(AlgorithmMode.GM);
    }

    public EncryptionContext(AlgorithmMode mode) {
        this.strategy = EncryptionStrategyFactory.getStrategy(mode);
    }

    public EncryptionContext(EncryptionStrategy strategy) {
        this.strategy = strategy;
    }

    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        return strategy.encrypt(sensitiveData, encryptionKey, macKey);
    }

    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        return strategy.decrypt(encryptedData, encryptionKey, macKey);
    }

    public String decrypt(String formattedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        EncryptionResult data = EncryptionResult.fromFormattedString(formattedData);
        this.strategy = EncryptionStrategyFactory.getStrategyForData(formattedData);
        return strategy.decrypt(data, encryptionKey, macKey);
    }

    public AlgorithmMode getCurrentAlgorithmMode() {
        return strategy.getAlgorithmMode();
    }
}
