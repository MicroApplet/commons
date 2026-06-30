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

import com.asialjim.microapplet.sensitive.mybatis.enc.BlindIndexGranularity;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 敏感数据加密配置
 * <p>从 {@code application.yml} 读取密钥配置。</p>
 *
 * <pre>{@code
 * sensitive:
 *   encrypt:
 *     mode: GM
 *     keys:
 *       - mode: GM
 *         encKey: base64编码的SM4密钥
 *         macKey: base64编码的HmacSM3密钥
 *       - mode: MODERN
 *         encKey: base64编码的ChaCha20密钥
 * }</pre>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "sensitive.encrypt")
public class SensitiveEncryptProperties implements AlgorithmModeConfig {
    /** 当前使用的算法模式 */
    private String mode = "GM";
    /** 各算法模式的密钥配置 */
    private List<SensitiveEncryptKeyProperty> keys;

    /** 盲索引切分粒度，默认二元组, 用于模糊查询分词 */
    private BlindIndexGranularity blindIndexGranularity = BlindIndexGranularity.BIGRAM;
    /** 盲索引 token 长度（base64url 字符数）,用户模糊查询分词，默认 8 */
    private int tokenLength = 8;

    @Override
    public AlgorithmMode getCurrentMode() {
        return AlgorithmMode.fromCode(mode);
    }

    @Bean
    public EncryptionContext encryptionContext(){
        return new EncryptionContext(getCurrentMode());
    }
}
