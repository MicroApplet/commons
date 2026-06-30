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

import com.asialjim.microapplet.sensitive.encrypt.AlgorithmMode;

import javax.crypto.SecretKey;

/**
 * 存储加密的子密钥集合。
 *
 * <p>由现有密钥经 {@link Hkdf} 派生，包含三把用途隔离的子密钥：</p>
 * <ul>
 *     <li>{@code valueKey} —— 值列确定性加密密钥</li>
 *     <li>{@code sivKey}   —— 合成 IV 推导密钥（SIV）</li>
 *     <li>{@code indexKey} —— 盲索引 token HMAC 密钥</li>
 * </ul>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public record StoreCryptoKeys(AlgorithmMode mode,
                              SecretKey valueKey,
                              byte[] sivKey,
                              byte[] indexKey) {
}
