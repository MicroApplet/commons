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

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 证书信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
public class CertificateInfo implements Serializable {
    /**
     * 应用编号
     */
    private String appid;

    /**
     * PEM格式证书文件内容
     */
    private byte[] serverCert;

    /**
     * SM4加密后的私钥
     */
    private byte[] encryptedPrivateKey;

    /**
     * 客户端公钥证书
     */
    private byte[] clientPubCert;

    /**
     * 证书创建时间
     */
    private LocalDateTime createTime;

    /**
     * 证书更新时间
     */
    private LocalDateTime updateTime;
}