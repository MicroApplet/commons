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

package io.github.microapplet.crypt;

import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.SM2Engine;
import org.bouncycastle.crypto.params.ECKeyParameters;
import org.bouncycastle.crypto.params.ParametersWithRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;

/**
 * 国密 SM2 加解密工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class Sm2CryptoService {
    private static final Provider BC_PROVIDER = new BouncyCastleProvider();

    // SM2加密
    public byte[] encrypt(PublicKey publicKey, byte[] plainText) throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        ECKeyParameters ecKey = (ECKeyParameters) publicKey;
        engine.init(true, new ParametersWithRandom(ecKey, new SecureRandom()));
        return engine.processBlock(plainText, 0, plainText.length);
    }

    // SM2解密
    public byte[] decrypt(PrivateKey privateKey, byte[] cipherText) throws InvalidCipherTextException {
        SM2Engine engine = new SM2Engine(SM2Engine.Mode.C1C3C2);
        engine.init(false, (ECKeyParameters) privateKey);
        return engine.processBlock(cipherText, 0, cipherText.length);
    }

    // 签名
    public byte[] sign(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SM3withSM2", BC_PROVIDER);
        sig.initSign(privateKey);
        sig.update(data);
        return sig.sign();
    }

    // 签名
    public String signStr(PrivateKey privateKey, byte[] data) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] sign = sign(privateKey, data);
        return Base64.toBase64String(sign);
    }

    // 验签
    public boolean verify(PublicKey publicKey, byte[] data, byte[] signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature sig = Signature.getInstance("SM3withSM2", BC_PROVIDER);
        sig.initVerify(publicKey);
        sig.update(data);
        return sig.verify(signature);
    }

    // 验签
    public boolean verify(PublicKey publicKey, byte[] data, String signature) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        byte[] sign = Base64.decode(signature);
        return verify(publicKey,data,sign);
    }
}