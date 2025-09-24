/*
 *    Copyright 2014-$year.today <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.web.mvc.advice;

import com.asialjim.microapplet.common.cons.Headers;
import com.asialjim.microapplet.common.context.Res;
import com.asialjim.microapplet.common.context.ResCode;
import com.asialjim.microapplet.common.context.Result;
import com.asialjim.microapplet.common.utils.JsonUtil;
import com.asialjim.microapplet.common.utils.XmlUtil;
import com.asialjim.microapplet.web.mvc.annotation.RwIgnore;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import jakarta.servlet.http.HttpServletRequest;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.*;

import static com.asialjim.microapplet.common.cons.Headers.*;

/**
 * 全局通用响应结果拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.asialjim")
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
    public boolean supports(MethodParameter returnType,
                            @SuppressWarnings("NullableProblems") Class<? extends HttpMessageConverter<?>> converterType) {

        Method method = returnType.getMethod();
        if (Objects.nonNull(method)) {
            return !method.isAnnotationPresent(RwIgnore.class)                                      // 没有避免包装标记
                    && !method.isAnnotationPresent(Async.class)                                     // 没有异步标记
                    && !ByteArrayHttpMessageConverter.class.isAssignableFrom(converterType)         // 不是二进制响应结果
                    && !BufferedImageHttpMessageConverter.class.isAssignableFrom(converterType);    // 不是图片
        }
        return true;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        Object o = doBeforeBody(body, request, response);

        // 特殊处理：使用StringHttpMessageConverter的情况
        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            if (MediaType.TEXT_PLAIN.includes(selectedContentType)) {
                o = JsonUtil.instance.toStr(o);
                response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            } else if (MediaType.APPLICATION_JSON.equalsTypeAndSubtype(selectedContentType)) {
                o = JsonUtil.instance.toStr(o);
            } else if (MediaType.APPLICATION_XML.equalsTypeAndSubtype(selectedContentType)) {
                o = XmlUtil.instance.toStr(o);
            } else {
                o = body;
            }
        }

        log.info("\r\n<<响应体: {}", o);
        return o;
    }

    private static Object doBeforeBody(Object body, ServerHttpRequest request, ServerHttpResponse response) {
        boolean skip = Optional.ofNullable(request)
                .map(HttpRequest::getURI)
                .map(URI::getPath)
                .map(skipUris::contains)
                .orElse(false);

        if (skip)
            return body;

        if (body instanceof ResponseEntity)
            return body;

        HttpHeaders requestHeaders = Optional.ofNullable(request).map(ServerHttpRequest::getHeaders).orElseGet(HttpHeaders::new);
        List<String> clientTypes = Optional.of(requestHeaders).map(item -> item.get(Headers.CLIENT_TYPE)).orElseGet(Collections::emptyList);
        // 云调用
        if (clientTypes.contains(Headers.CLOUD_CLIENT)) {
            //noinspection rawtypes
            if (body instanceof Result result) {
                response.setStatusCode(HttpStatusCode.valueOf(result.getStatus()));
                HttpHeaders headers = response.getHeaders();
                headers.set(X_RES_THROWABLE, String.valueOf(result.isThr()));
                headers.set(X_RES_CODE, result.getCode());
                headers.set(X_RES_MSG, result.getMsg());
                headers.set(X_RES_ERRS, JsonUtil.instance.toStr(result.getErrs()));
                headers.set(X_RES_STATUS, String.valueOf(result.getStatus()));
                headers.set(X_RES_PAGE, String.valueOf(result.getPage()));
                headers.set(X_RES_SIZE, String.valueOf(result.getSize()));
                headers.set(X_RES_PAGES, String.valueOf(result.getPages()));
                headers.set(X_RES_TOTAL, String.valueOf(result.getTotal()));
                return result.getData();
            }
            return body;
        }

        // 如果已经是 Result，则不再包装
        //noinspection rawtypes
        if (body instanceof Result result) {
            int status = result.getStatus();
            response.setStatusCode(HttpStatusCode.valueOf(status));
            return result;
        }

        if (body instanceof ResCode cd) {
            int status = cd.getStatus();
            response.setStatusCode(HttpStatusCode.valueOf(status));
            return cd.result();
        }

        return Res.OK.result(body);
    }
}