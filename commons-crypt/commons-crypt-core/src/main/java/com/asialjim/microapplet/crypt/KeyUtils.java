/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.crypt;


import org.bouncycastle.crypto.CryptoException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * 证书密钥处理工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class KeyUtils {

    private static final String SM4_KEY = "secure_key_placeholder"; // 应从KMS获取
    private static final String SM4_IV = "secure_key_iv";

    public static PrivateKey decryptPrivateKey(byte[] encryptedKey) throws CryptoException {
        try {
            byte[] decrypted = decrypt(encryptedKey);
            return KeyFactory.getInstance("EC", "BC")
                    .generatePrivate(new PKCS8EncodedKeySpec(decrypted));
        } catch (Exception e) {
            throw new CryptoException("私钥解密失败", e);
        }
    }

    public static PublicKey parsePublicKey(byte[] certBytes) throws CryptoException {
        try (InputStream is = new ByteArrayInputStream(certBytes)) {
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) cf.generateCertificate(is);
            return cert.getPublicKey();
        } catch (Exception e) {
            throw new CryptoException("公钥解析失败", e);
        }
    }


    private static byte[] decrypt(byte[] encryptedKey) throws NoSuchPaddingException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        // 解密参数设置（需与加密时一致）
        String algorithm = "SM4/CBC/PKCS7Padding"; // 加密模式及填充方式
        // 16字节密钥（128位）
        byte[] keyBytes = SM4_KEY.getBytes(StandardCharsets.UTF_8);
        // 16字节IV（CBC模式需要）
        byte[] ivBytes = SM4_IV.getBytes(StandardCharsets.UTF_8);

        // 构建密钥和IV参数
        SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "SM4");
        IvParameterSpec ivParams = new IvParameterSpec(ivBytes);

        // 初始化Cipher为解密模式
        Cipher cipher = Cipher.getInstance(algorithm, "BC");
        cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParams);

        // 执行解密
        return cipher.doFinal(encryptedKey);
    }
}