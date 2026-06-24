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

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Objects;

/**
 * 国密算法策略实现 — SM4加密 + HmacSM3消息认证
 */
public class GMEncryptionStrategy implements EncryptionStrategy {
    private static final String CIPHER = "SM4/CBC/PKCS5Padding";
    private static final String MAC_ALG = "HmacSM3";
    private static final int IV_LEN = 16;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey) || Objects.isNull(macKey))
            throw new IllegalArgumentException("国密算法需要加密密钥和MAC密钥");

        byte[] iv = new byte[IV_LEN];
        secureRandom.nextBytes(iv);

        // SM4 加密
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(iv));
        byte[] encrypted = cipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));

        // HmacSM3 认证
        Mac mac = Mac.getInstance(MAC_ALG);
        mac.init(macKey);
        mac.update(encrypted);
        byte[] macBytes = mac.doFinal();

        return new EncryptionResult(AlgorithmMode.GM, iv, encrypted, macBytes);
    }

    @Override
    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey) || Objects.isNull(macKey))
            throw new IllegalArgumentException("国密算法需要加密密钥和MAC密钥");

        // 验证 MAC
        Mac mac = Mac.getInstance(MAC_ALG);
        mac.init(macKey);
        mac.update(encryptedData.encrypt());
        byte[] calculatedMac = mac.doFinal();
        if (!MessageDigest.isEqual(calculatedMac, encryptedData.mac()))
            throw new SecurityException("MAC验证失败，数据可能被篡改");

        // SM4 解密
        Cipher cipher = Cipher.getInstance(CIPHER);
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, new IvParameterSpec(encryptedData.nonce()));
        byte[] decrypted = cipher.doFinal(encryptedData.encrypt());
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @Override
    public AlgorithmMode getAlgorithmMode() { return AlgorithmMode.GM; }

    @Override
    public boolean supports(String formattedData) {
        try {
            return formattedData.split("\\|").length >= 2 && AlgorithmMode.GM.getCode().equals(formattedData.split("\\|")[1]);
        } catch (Exception e) {
            return false;
        }
    }
}
