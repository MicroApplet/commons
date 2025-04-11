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

package com.asialjim.microapplet.commons.config.core;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.TreeSet;

/**
 * 通用配置信息包扫描
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
@ComponentScan
public class TypedConfigBean {
    @Bean
    @ConditionalOnMissingBean
    public ConfPropertyRepository confPropertyRepository(){
        return new ConfPropertyRepository(){
            @Override
            public TreeSet<ConfProperty> query(ConfType type, String business, String code, Env env) {
                throw new UnsupportedOperationException("通用配置信息仓库：ConfPropertyRepository 未配置");
            }

            @Override
            public void put(ConfProperty confProperty) {
                throw new UnsupportedOperationException("通用配置信息仓库：ConfPropertyRepository 未配置");
            }
        };
    }
}