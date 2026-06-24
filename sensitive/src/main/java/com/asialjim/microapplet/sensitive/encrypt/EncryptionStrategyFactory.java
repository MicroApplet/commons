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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 加密策略工厂
 */
public class EncryptionStrategyFactory {
    private static final Map<AlgorithmMode, EncryptionStrategy> STRATEGY_MAP = new ConcurrentHashMap<>();

    static {
        STRATEGY_MAP.put(AlgorithmMode.GM, new GMEncryptionStrategy());
        STRATEGY_MAP.put(AlgorithmMode.MODERN, new ModernEncryptionStrategy());
    }

    public static EncryptionStrategy getStrategy(AlgorithmMode mode) {
        EncryptionStrategy strategy = STRATEGY_MAP.get(mode);
        if (strategy == null)
            throw new IllegalArgumentException("不支持的算法模式: " + mode);
        return strategy;
    }

    public static EncryptionStrategy getStrategyForData(String formattedData) {
        if (formattedData == null || formattedData.isBlank())
            return getStrategy(AlgorithmMode.GM);
        String[] parts = formattedData.split("\\|");
        if (parts.length >= 2) {
            try {
                AlgorithmMode mode = AlgorithmMode.fromCode(parts[1]);
                return getStrategy(mode);
            } catch (IllegalArgumentException ignored) {
            }
        }
        return getStrategy(AlgorithmMode.GM);
    }
}
