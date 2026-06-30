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
import com.asialjim.microapplet.sensitive.encrypt.SecretKeyRepository;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 存储子密钥仓库。
 *
 * <p>从现有 {@link SecretKeyRepository} 取出传输用 encKey/macKey，经 HKDF 派生出
 * 存储专用子密钥（{@link StoreCryptoKeys}），并按算法模式缓存。</p>
 *
 * <p>派生标签固定，保证同一组配置密钥每次派生结果一致：</p>
 * <ul>
 *     <li>{@code store:value} —— 值列加密密钥</li>
 *     <li>{@code store:siv}   —— 合成 IV 密钥</li>
 *     <li>{@code store:index} —— 盲索引密钥</li>
 * </ul>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public class StoreKeyRepository {
    private static final int IV_KEY_LEN = 32;
    private static final int INDEX_KEY_LEN = 32;

    private final SecretKeyRepository delegate;
    private final Map<AlgorithmMode, StoreCryptoKeys> cache = new ConcurrentHashMap<>();

    public StoreKeyRepository(SecretKeyRepository delegate) {
        this.delegate = delegate;
    }

    public StoreCryptoKeys keysOf(AlgorithmMode mode) {
        return cache.computeIfAbsent(mode, this::derive);
    }

    private StoreCryptoKeys derive(AlgorithmMode mode) {
        try {
            SecretKeyRepository.Pair pair = delegate.pairOf(mode);
            byte[] encIkm = pair.getEncKey().getEncoded();
            byte[] macIkm = pair.getMacKey() != null
                    ? pair.getMacKey().getEncoded()
                    : pair.getEncKey().getEncoded();

            int valueKeyBytes = pair.getEncKey().getEncoded().length;
            byte[] valueRaw = Hkdf.derive(encIkm, null, Hkdf.info("store:value:" + mode.getCode()), valueKeyBytes);
            SecretKey valueKey = new SecretKeySpec(valueRaw, storeCipherAlgorithm(mode));

            byte[] sivKey = Hkdf.derive(macIkm, null, Hkdf.info("store:siv:" + mode.getCode()), IV_KEY_LEN);
            byte[] indexKey = Hkdf.derive(macIkm, null, Hkdf.info("store:index:" + mode.getCode()), INDEX_KEY_LEN);

            return new StoreCryptoKeys(mode, valueKey, sivKey, indexKey);
        } catch (Exception e) {
            throw new IllegalStateException("存储子密钥派生失败: mode=" + mode.getCode(), e);
        }
    }

    /**
     * 存储用对称算法名。
     * <p>注意：MODERN 在传输方案里用流密码 ChaCha20，不适合确定性分组加密；
     * 存储场景统一改用分组密码 CBC（GM=SM4，MODERN=AES），故 valueKey 的算法名按存储算法标定。</p>
     */
    private String storeCipherAlgorithm(AlgorithmMode mode) {
        return switch (mode) {
            case GM -> "SM4";
            case MODERN -> "AES";
        };
    }
}
