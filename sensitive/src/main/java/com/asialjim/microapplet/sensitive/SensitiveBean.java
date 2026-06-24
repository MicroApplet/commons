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

package com.asialjim.microapplet.sensitive;

import com.asialjim.microapplet.sensitive.encrypt.SensitiveEncryptProperties;
import com.asialjim.microapplet.sensitive.mybatis.SensitiveInterceptor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 敏感数据处理自动配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Configuration
@EnableConfigurationProperties(SensitiveEncryptProperties.class)
@ComponentScan(basePackages = {
        "com.asialjim.microapplet.sensitive",
        "com.asialjim.microapplet.sensitive.encrypt",
        "com.asialjim.microapplet.sensitive.handler",
        "com.asialjim.microapplet.sensitive.jackson",
        "com.asialjim.microapplet.sensitive.mybatis"
})
public class SensitiveBean {

    @Bean
    public SensitiveInterceptor sensitiveInterceptor() {
        return new SensitiveInterceptor();
    }
}
