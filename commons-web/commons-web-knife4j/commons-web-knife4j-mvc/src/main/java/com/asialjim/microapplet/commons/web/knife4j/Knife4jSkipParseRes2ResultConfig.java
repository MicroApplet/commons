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

package com.asialjim.microapplet.commons.web.knife4j;

import com.asialjim.microapplet.web.mvc.config.SkipParseRes2ResultConfig;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.SpringDocConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

/**
 * Knife4j 文档不对响应结果进行包装
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
@ComponentScan
public class Knife4jSkipParseRes2ResultConfig implements SkipParseRes2ResultConfig {
    private SpringDocConfigProperties springDocConfigProperties;

    @Autowired(required = false)
    public void setSpringDocConfigProperties(SpringDocConfigProperties springDocConfigProperties) {
        this.springDocConfigProperties = springDocConfigProperties;
    }

    @Override
    public boolean skipParseRes2ResultCheck(String url, String context) {
        String docUri = Optional.ofNullable(this.springDocConfigProperties)
                .map(SpringDocConfigProperties::getApiDocs)
                .map(SpringDocConfigProperties.ApiDocs::getPath)
                .orElse(StringUtils.EMPTY);
        if (StringUtils.isBlank(docUri))
            return false;

        return StringUtils.startsWith(url, context + docUri);
    }
}