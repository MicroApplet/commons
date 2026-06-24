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

package com.asialjim.microapplet.session;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户加密通讯密钥。
 * <p>存储用户请求体的加密密钥信息，用于网关层解密客户端请求。</p>
 */
@Data
public class MamsUserEncrypt implements Serializable {
    /** 加密类型，如 "AES" */
    private String encryptType;
    /** 用户密钥 */
    private String userKey;
    /** 初始向量 IV */
    private String iv;
}
