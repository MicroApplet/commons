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
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;

/**
 * 确定性存储加密器（合成 IV / SIV 思想）。
 *
 * <p>同一明文恒得同一密文，保证唯一索引、等值查询、去重可用；密文紧凑，无传输方案的信封与掩码。</p>
 *
 * <p>密文格式（base64url）：</p>
 * <pre>
 *   byte[0]      = 版本号（当前 1）
 *   byte[1]      = 算法模式 ordinal（GM/MODERN）
 *   byte[2..17]  = 合成 IV（16 字节，由 HMAC(sivKey, plaintext) 截断）
 *   byte[18..]   = 密文
 * </pre>
 *
 * <p>对外存储前缀 {@value #PREFIX}，便于识别“已是存储密文”，避免重复加密。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public class StoreEncryptor {
    /** 存储密文前缀，区别于传输方案的 {@code _mask} */
    public static final String PREFIX = "_st:";
    private static final byte VERSION = 1;
    private static final int IV_LEN = 16;
    private static final String SIV_MAC = "HmacSHA256";

    private final StoreKeyRepository keyRepository;

    public StoreEncryptor(StoreKeyRepository keyRepository) {
        this.keyRepository = keyRepository;
    }

    /** 是否为本方案生成的存储密文 */
    public static boolean isStoreCipher(String data) {
        return StringUtils.startsWith(data, PREFIX);
    }

    public String encrypt(AlgorithmMode mode, String plaintext) {
        if (StringUtils.isEmpty(plaintext))
            return plaintext;
        if (isStoreCipher(plaintext))
            return plaintext; // 已是存储密文，避免重复加密

        try {
            StoreCryptoKeys keys = keyRepository.keysOf(mode);
            byte[] plain = plaintext.getBytes(StandardCharsets.UTF_8);
            byte[] iv = syntheticIv(keys.sivKey(), plain);
            byte[] cipherText = transform(Cipher.ENCRYPT_MODE, mode, keys, iv, plain);

            byte[] out = new byte[2 + IV_LEN + cipherText.length];
            out[0] = VERSION;
            out[1] = (byte) mode.ordinal();
            System.arraycopy(iv, 0, out, 2, IV_LEN);
            System.arraycopy(cipherText, 0, out, 2 + IV_LEN, cipherText.length);
            return PREFIX + Base64.getUrlEncoder().withoutPadding().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("存储加密失败", e);
        }
    }

    public String decrypt(String cipher) {
        if (StringUtils.isEmpty(cipher) || !isStoreCipher(cipher))
            return cipher;

        try {
            byte[] raw = Base64.getUrlDecoder().decode(cipher.substring(PREFIX.length()));
            byte version = raw[0];
            if (version != VERSION)
                throw new IllegalStateException("不支持的存储密文版本: " + version);

            AlgorithmMode mode = AlgorithmMode.values()[raw[1]];
            byte[] iv = Arrays.copyOfRange(raw, 2, 2 + IV_LEN);
            byte[] cipherText = Arrays.copyOfRange(raw, 2 + IV_LEN, raw.length);

            StoreCryptoKeys keys = keyRepository.keysOf(mode);
            byte[] plain = transform(Cipher.DECRYPT_MODE, mode, keys, iv, cipherText);
            return new String(plain, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new IllegalStateException("存储解密失败", e);
        }
    }

    /** 合成 IV：HMAC(sivKey, 明文) 截断到 IV 长度，明文相同则 IV 相同，从而密文确定 */
    private byte[] syntheticIv(byte[] sivKey, byte[] plain) throws Exception {
        Mac mac = Mac.getInstance(SIV_MAC);
        mac.init(new SecretKeySpec(sivKey, SIV_MAC));
        byte[] full = mac.doFinal(plain);
        return Arrays.copyOf(full, IV_LEN);
    }

    private byte[] transform(int cipherMode, AlgorithmMode mode, StoreCryptoKeys keys, byte[] iv, byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance(transformation(mode));
        cipher.init(cipherMode, keys.valueKey(), new IvParameterSpec(iv));
        return cipher.doFinal(data);
    }

    private String transformation(AlgorithmMode mode) {
        return switch (mode) {
            case GM -> "SM4/CBC/PKCS5Padding";
            case MODERN -> "AES/CBC/PKCS5Padding";
        };
    }
}
