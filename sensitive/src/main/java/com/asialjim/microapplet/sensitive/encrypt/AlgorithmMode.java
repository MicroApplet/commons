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

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 算法模式
 */
@Getter
@AllArgsConstructor
public enum AlgorithmMode {
    GM("GM", "国密算法(SM4-SM3)", "SM4", "HmacSM3"),
    MODERN("MODERN", "现代算法(ChaCha20-Poly1305)", "ChaCha20", "ChaCha20");

    private final String code;
    private final String description;
    private final String encAlgorithm;
    private final String macAlgorithm;

    public static AlgorithmMode fromCode(String code) {
        return Arrays.stream(values())
                .filter(m -> m.code.equals(code))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("未知的算法模式: " + code));
    }
}
