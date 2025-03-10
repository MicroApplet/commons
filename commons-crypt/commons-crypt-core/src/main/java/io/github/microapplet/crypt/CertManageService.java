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

import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.util.encoders.Base64;

import java.security.*;

/**
 * 证书管理服务
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */

public class CertManageService {

    private CertificateRepository certRepo;

    private Sm2CryptoService cryptoService;

    public CertificateInfo getCertInfo(String appid) {
        return certRepo.findById(appid);
    }

    public byte[] decryptRequest(String appid, byte[] cipherText) throws CryptoException {
        CertificateInfo certInfo = getCertInfo(appid);
        PrivateKey privateKey = KeyUtils.decryptPrivateKey(certInfo.getEncryptedPrivateKey());
        return cryptoService.decrypt(privateKey, cipherText);
    }

    public boolean verifyRequest(String appid, byte[] cipherText, byte[] signature) throws CryptoException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        CertificateInfo certInfo = getCertInfo(appid);
        byte[] serverCert = certInfo.getServerCert();
        PublicKey publicKey = KeyUtils.parsePublicKey(serverCert);
        return cryptoService.verify(publicKey, cipherText, signature);
    }


    public byte[] encryptResponse(String appid, byte[] data) throws CryptoException {
        CertificateInfo certInfo = getCertInfo(appid);
        PublicKey clientPubKey = KeyUtils.parsePublicKey(certInfo.getClientPubCert());
        return cryptoService.encrypt(clientPubKey, data);
    }


    public String signResponse(String appid, byte[] data) throws CryptoException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        CertificateInfo certInfo = getCertInfo(appid);
        PrivateKey privateKey = KeyUtils.decryptPrivateKey(certInfo.getEncryptedPrivateKey());
        byte[] signature = cryptoService.sign(privateKey, data);
        return Base64.toBase64String(signature);
    }
}
