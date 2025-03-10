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

package io.github.microapplet.web.mvc.config;

import io.github.microapplet.common.context.Res;
import io.github.microapplet.common.context.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * 全局通用响应结果拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@ControllerAdvice
public class GlobalResponseAspect implements ResponseBodyAdvice<Object> {
    private static final String SKIP_PARSE_RES_TO_RESULT = "_skip_parse_res_to_result_";
    private static final Set<String> skipUris = new HashSet<>();


    public static void skip(HttpServletRequest ctx, Boolean skip, String uri) {
        ctx.setAttribute(SKIP_PARSE_RES_TO_RESULT, skip);
        if (Boolean.TRUE.equals(skip))
            skipUris.add(uri);
    }

    public static void clean(String uri) {
        if (StringUtils.isBlank(uri))
            skipUris.remove(uri);
    }

    @Override
    public boolean supports(@SuppressWarnings("NullableProblems") MethodParameter returnType,
                            @SuppressWarnings("NullableProblems") Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  @SuppressWarnings("NullableProblems") MethodParameter returnType,
                                  @SuppressWarnings("NullableProblems") MediaType mediaType,
                                  @SuppressWarnings("NullableProblems") Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  @SuppressWarnings("NullableProblems") ServerHttpRequest request,
                                  @SuppressWarnings("NullableProblems") ServerHttpResponse response) {


        //noinspection OptionalOfNullableMisuse
        Boolean skip = Optional.ofNullable(request)
                .map(HttpRequest::getURI)
                .map(URI::getPath)
                .map(skipUris::contains)
                .orElse(false);
        if (Boolean.TRUE.equals(skip))
            return body;

        if (body instanceof Result)
            return body;

        //noinspection OptionalOfNullableMisuse
        String subType = Optional.ofNullable(mediaType).map(MediaType::getSubtype).orElse("");

        if (StringUtils.equalsAnyIgnoreCase(subType, "json", "xml"))
            return Res.Ok.create(body);
        return body;
    }
}