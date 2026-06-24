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
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;

/**
 * 现代算法策略实现 — ChaCha20-Poly1305（AEAD，自带认证，无需额外 MAC）
 */
public class ModernEncryptionStrategy implements EncryptionStrategy {
    private static final String ALGORITHM = "ChaCha20-Poly1305";
    private static final int NONCE_LEN = 12;
    private static final int TAG_BITS = 128;

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public EncryptionResult encrypt(String sensitiveData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey))
            throw new IllegalArgumentException("现代算法需要加密密钥");

        byte[] nonce = new byte[NONCE_LEN];
        secureRandom.nextBytes(nonce);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, encryptionKey, new IvParameterSpec(nonce));
        byte[] ciphertextWithTag = cipher.doFinal(sensitiveData.getBytes(StandardCharsets.UTF_8));

        int ctLen = ciphertextWithTag.length - TAG_BITS / 8;
        byte[] encrypted = Arrays.copyOf(ciphertextWithTag, ctLen);
        byte[] mac = Arrays.copyOfRange(ciphertextWithTag, ctLen, ciphertextWithTag.length);

        return new EncryptionResult(AlgorithmMode.MODERN, nonce, encrypted, mac);
    }

    @Override
    public String decrypt(EncryptionResult encryptedData, SecretKey encryptionKey, SecretKey macKey) throws Exception {
        if (Objects.isNull(encryptionKey))
            throw new IllegalArgumentException("现代算法需要加密密钥");

        byte[] combined = new byte[encryptedData.encrypt().length + encryptedData.mac().length];
        System.arraycopy(encryptedData.encrypt(), 0, combined, 0, encryptedData.encrypt().length);
        System.arraycopy(encryptedData.mac(), 0, combined, encryptedData.encrypt().length, encryptedData.mac().length);

        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, encryptionKey, new IvParameterSpec(encryptedData.nonce()));
        byte[] decrypted = cipher.doFinal(combined);
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    @Override
    public AlgorithmMode getAlgorithmMode() { return AlgorithmMode.MODERN; }

    @Override
    public boolean supports(String formattedData) {
        try {
            return formattedData.split("\\|").length >= 2 && AlgorithmMode.MODERN.getCode().equals(formattedData.split("\\|")[1]);
        } catch (Exception e) {
            return false;
        }
    }
}
