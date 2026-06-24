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

import com.asialjim.microapplet.commons.standard.utils.SessionTokenUtil;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 会话令牌密钥配置。
 * <p>从 {@code application.yml} 读取令牌签名密钥，自动注入到 {@link SessionTokenUtil}。</p>
 *
 * <pre>{@code
 * microapplet:
 *   token:
 *     secret: 16位密钥字符串
 * }</pre>
 */
@Data
@ConfigurationProperties(prefix = "microapplet.token")
public class SessionTokenProperties {

    /** 令牌签名密钥（建议 16 位或以上） */
    private String secret;

    @PostConstruct
    public void init() {
        if (secret != null && !secret.isBlank()) {
            SessionTokenUtil.setConfiguredSecret(secret);
        }
    }
}
