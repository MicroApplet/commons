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

import com.asialjim.microapplet.common.cons.Headers;
import com.asialjim.microapplet.common.exception.RsEx;
import com.asialjim.microapplet.common.utils.JsonUtil;
import feign.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import jakarta.servlet.http.HttpServletRequest;

import static com.asialjim.microapplet.common.cons.Headers.*;

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
            String trace = MDC.get(Headers.TRACE_ID);
            template.header(Headers.TRACE_ID, trace);
            template.header(Headers.CLIENT_TYPE, Headers.CLOUD_CLIENT);

            RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            if (Objects.isNull(requestAttributes))
                return;

            if (requestAttributes instanceof ServletRequestAttributes attributes) {
                HttpServletRequest request = attributes.getRequest();
                String currentUserJson = request.getHeader(Headers.BASE_CURRENT_USER);
                if (StringUtils.isNotBlank(currentUserJson))
                    template.header(Headers.BASE_CURRENT_USER, currentUserJson);
                String token = request.getHeader(Headers.AUTH_PARAMETERS_HEADER);
                if (StringUtils.isNotBlank(token))
                    template.header(Headers.AUTH_PARAMETERS_HEADER, token);

                for (String header : Headers.headers) {
                    String headerValue = request.getHeader(header);
                    if (StringUtils.isBlank(headerValue))
                        continue;
                    template.header(header, headerValue);
                }
            }

            template.header(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
            template.header(HttpHeaders.USER_AGENT, Headers.CloudAgent);
        };
    }

    @Bean
    public SpringDecoder feignResponseInterceptor(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new SpringDecoder(messageConverters) {

            @Override
            public Object decode(Response response, Type type) throws IOException, FeignException {
                Map<String, Collection<String>> headers = response.headers();

                int status = NumberUtils.toInt(header(headers, X_RES_STATUS), response.status());
                boolean thr = thr(headers);

                String code = header(headers, X_RES_CODE);
                String msg = header(headers, X_RES_MSG);
                List<Object> errs = JsonUtil.instance.toList(header(headers, X_RES_ERRS), Object.class);

                if (status >= 400)
                    new RsEx().setStatus(status).setThr(thr).setCode(code).setMsg(msg).setErrs(errs).cast();

                return super.decode(response, type);
            }

            private boolean thr(Map<String,Collection<String>>headers){
                String thr = header(headers, X_RES_THROWABLE);
                if (StringUtils.isNotBlank(thr))
                    return Boolean.parseBoolean(thr);
                return false;
            }

            private String header(Map<String, Collection<String>> headers, String key) {
                Collection<String> strings = headers.get(key);
                String value = StringUtils.EMPTY;
                for (String string : strings) {
                    if (StringUtils.isNotBlank(string)) {
                        value = string;
                        break;
                    }
                }

                return value;
            }
        };
    }
}