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
import com.asialjim.microapplet.sensitive.encrypt.AlgorithmModeConfig;

import java.util.List;

/**
 * 数据库存储加密门面。
 *
 * <p>统一对外提供存储场景的三类能力，是 MyBatis 层后续适配的唯一入口：</p>
 * <ol>
 *     <li>{@link #encrypt(String)} —— 值列确定性加密（写入）</li>
 *     <li>{@link #decrypt(String)} —— 值列解密（读取）</li>
 *     <li>{@link #blindIndex(String)} —— 生成盲索引列内容（写入需模糊查询的字段）</li>
 *     <li>{@link #queryTokens(String)} —— 把查询关键字转为盲索引 token（模糊查询改写）</li>
 * </ol>
 *
 * <p>算法模式取自现有 {@link AlgorithmModeConfig#getCurrentMode()}，与传输方案保持同一模式来源。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public class StoreCipher {
    private final AlgorithmModeConfig algorithmModeConfig;
    private final StoreEncryptor encryptor;
    private final BlindIndexTokenizer tokenizer;

    public StoreCipher(AlgorithmModeConfig algorithmModeConfig,
                       StoreEncryptor encryptor,
                       BlindIndexTokenizer tokenizer) {
        this.algorithmModeConfig = algorithmModeConfig;
        this.encryptor = encryptor;
        this.tokenizer = tokenizer;
    }

    /** 值列确定性加密 */
    public String encrypt(String plaintext) {
        return encryptor.encrypt(mode(), plaintext);
    }

    /** 值列解密 */
    public String decrypt(String cipher) {
        return encryptor.decrypt(cipher);
    }

    /** 是否为存储密文 */
    public boolean isStoreCipher(String data) {
        return StoreEncryptor.isStoreCipher(data);
    }

    /** 生成盲索引列内容 */
    public String blindIndex(String plaintext) {
        return tokenizer.index(mode(), plaintext);
    }

    /** 查询关键字转盲索引 token（用于模糊查询条件改写） */
    public List<String> queryTokens(String keyword) {
        return tokenizer.queryTokens(mode(), keyword);
    }

    private AlgorithmMode mode() {
        return algorithmModeConfig.getCurrentMode();
    }
}
