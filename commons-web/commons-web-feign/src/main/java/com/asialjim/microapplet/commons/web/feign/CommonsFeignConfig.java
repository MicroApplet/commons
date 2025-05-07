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

package com.asialjim.microapplet.commons.web.feign;

import com.asialjim.microapplet.common.cons.HttpHeaderCons;
import feign.Logger;
import feign.RequestInterceptor;
import feign.Retryer;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import javax.servlet.http.HttpServletRequest;

/**
 * 通用 Feign 配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
public class CommonsFeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // 开启详细日志
    }

    @Bean
    public Retryer retryer() {
        return new Retryer.Default(100, 1000, 3); // 重试策略
    }

    @Bean
    public RequestInterceptor requestInterceptorWithAgentAndTraceId() {
        return template -> {
            try {
                RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
                if (Objects.nonNull(requestAttributes)) {
                    ServletRequestAttributes request = (ServletRequestAttributes) requestAttributes;
                    HttpServletRequest httpServletRequest = request.getRequest();
                    for (String name : HttpHeaderCons.headers) {
                        String header = httpServletRequest.getHeader(name);
                        if (StringUtils.isBlank(header))
                            continue;
                        template.header(name,header);
                    }
                }
            } catch (Throwable ignored) {

            }

            template.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            template.header(HttpHeaders.USER_AGENT, HttpHeaderCons.CloudAgent);
        };
    }
}