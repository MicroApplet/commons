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

package com.asialjim.microapplet.spring.jackson;

import com.asialjim.microapplet.commons.standard.utils.JacksonUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.SerializationFeature;
import tools.jackson.databind.cfg.DateTimeFeature;

/**
 * Jackson 配置 — 基于三代 Jackson（tools.jackson）
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Configuration
public class JacksonConfig {


    /**
     * 自定义 Jackson ObjectMapper：注册时间模块、配置序列化/反序列化特性
     */
   /*
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            // 注册自定义 Java Time 模块
            builder.modules(JacksonUtil.getModules());

            // 禁用序列化特性：空 Bean 报错、日期时间戳
            builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
            builder.featuresToDisable(DateTimeFeature.WRITE_DATES_AS_TIMESTAMPS);

            // 禁用反序列化特性：未知属性报错
            builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

            // 启用反序列化特性：忽略的属性报错、空字符串视为 null
            builder.featuresToEnable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
            builder.featuresToEnable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

            // 设置默认时区
            builder.timeZone("Asia/Shanghai");
        };
    }
    */
}
