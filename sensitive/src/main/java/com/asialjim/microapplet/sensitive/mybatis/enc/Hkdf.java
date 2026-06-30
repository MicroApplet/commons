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

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * HKDF（RFC 5869）密钥派生工具，基于 HmacSHA256。
 *
 * <p>用于从现有传输密钥派生出存储专用子密钥，实现“同源密钥、用途隔离”。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
final class Hkdf {
    private static final String HMAC = "HmacSHA256";
    private static final int HASH_LEN = 32;

    private Hkdf() {
    }

    /**
     * HKDF 完整流程：extract + expand。
     *
     * @param ikm    输入密钥材料（现有密钥字节）
     * @param salt   盐值，可为 null
     * @param info   用途上下文标签，区分不同子密钥
     * @param length 期望输出字节数
     */
    static byte[] derive(byte[] ikm, byte[] salt, byte[] info, int length) {
        byte[] prk = extract(salt, ikm);
        return expand(prk, info, length);
    }

    private static byte[] extract(byte[] salt, byte[] ikm) {
        if (salt == null || salt.length == 0)
            salt = new byte[HASH_LEN];
        return hmac(salt, ikm);
    }

    private static byte[] expand(byte[] prk, byte[] info, int length) {
        if (info == null)
            info = new byte[0];

        int n = (int) Math.ceil((double) length / HASH_LEN);
        if (n > 255)
            throw new IllegalArgumentException("HKDF 输出长度过大");

        byte[] okm = new byte[n * HASH_LEN];
        byte[] t = new byte[0];
        for (int i = 1; i <= n; i++) {
            byte[] input = new byte[t.length + info.length + 1];
            System.arraycopy(t, 0, input, 0, t.length);
            System.arraycopy(info, 0, input, t.length, info.length);
            input[input.length - 1] = (byte) i;
            t = hmac(prk, input);
            System.arraycopy(t, 0, okm, (i - 1) * HASH_LEN, HASH_LEN);
        }
        return Arrays.copyOf(okm, length);
    }

    private static byte[] hmac(byte[] key, byte[] data) {
        try {
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(key, HMAC));
            return mac.doFinal(data);
        } catch (Exception e) {
            throw new IllegalStateException("HKDF 派生失败", e);
        }
    }

    static byte[] info(String label) {
        return label.getBytes(StandardCharsets.UTF_8);
    }
}
