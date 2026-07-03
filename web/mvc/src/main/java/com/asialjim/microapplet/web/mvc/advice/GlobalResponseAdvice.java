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

package com.asialjim.microapplet.web.mvc.advice;

import com.asialjim.microapplet.commons.standard.context.Res;
import com.asialjim.microapplet.commons.standard.context.ResCode;
import com.asialjim.microapplet.commons.standard.context.Result;
import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import com.asialjim.microapplet.web.client.MamsHttpHeaders;
import com.asialjim.microapplet.web.mvc.annotation.RwIgnore;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.Nullable;
import org.springframework.core.MethodParameter;
import org.springframework.http.*;
import org.springframework.http.converter.*;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter;
import org.springframework.http.converter.xml.JacksonXmlHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.asialjim.microapplet.web.client.MamsHttpHeaders.*;

/**
 * 全局响应结果包装器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.asialjim.microapplet")
public class GlobalResponseAdvice implements ResponseBodyAdvice<Object> {

    private final JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter;
    private final JacksonXmlHttpMessageConverter jacksonXmlHttpMessageConverter;

    public GlobalResponseAdvice(
            @Nullable JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter,
            @Nullable JacksonXmlHttpMessageConverter jacksonXmlHttpMessageConverter) {
        this.jacksonJsonHttpMessageConverter = jacksonJsonHttpMessageConverter;
        this.jacksonXmlHttpMessageConverter = jacksonXmlHttpMessageConverter;
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SneakyThrows
    @SuppressWarnings("NullableProblems")
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {

        boolean exceptionHappen = exceptionHappen(returnType);
        boolean support = doSupports(returnType, selectedConverterType, selectedContentType);
        Object o = support
                ? doBefore(exceptionHappen, body, returnType, selectedContentType, selectedConverterType, request, response)
                : body;

        // 复制响应头到 HttpServletResponse
        HttpHeaders responseHeaders = response.getHeaders();
        if (response instanceof ServletServerHttpResponse servletServerHttpResponse) {
            HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
            responseHeaders.forEach((k, v) -> {
                if (StringUtils.isNotBlank(k)) {
                    for (String s : v) {
                        if (StringUtils.isNotBlank(s))
                            servletResponse.setHeader(k, s);
                    }
                }
            });
        }

        if (Objects.nonNull(o)) {
            log.info("\r\n<<响应体: \r\n\t{}", o);
        }
        return o;
    }

    private Object doBefore(boolean exceptionHappen, Object body, MethodParameter returnType,
                            MediaType selectedContentType,
                            Class<? extends HttpMessageConverter<?>> selectedConverterType,
                            ServerHttpRequest request, ServerHttpResponse response) {
        HttpHeaders headers = Optional.of(request).map(HttpMessage::getHeaders).orElseGet(HttpHeaders::new);
        List<String> clientTypes = Optional.of(headers)
                .map(item -> item.get(MamsHttpHeaders.HTTP_CLIENT_TYPE))
                .orElseGet(Collections::emptyList);

        // 内部 RPC 调用（cloudclient/loadbalance）
        if (clientTypes.contains(MamsHttpHeaders.LOAD_BALANCE_CLIENT))
            return wrapForInternal(exceptionHappen, body, returnType.getParameterType(), response);

        // 对外 WEB 调用
        return wrapForWeb(body, selectedContentType, selectedConverterType, request, response);
    }

    @SneakyThrows
    private Object wrapForWeb(Object body, MediaType selectedContentType,
                              Class<? extends HttpMessageConverter<?>> selectedConverterType,
                              ServerHttpRequest request, ServerHttpResponse response) {
        Result<?> o = doWrap(body, response);
        if (Objects.isNull(o))
            return null;

        // StringHttpMessageConverter 特殊处理
        if (StringHttpMessageConverter.class.isAssignableFrom(selectedConverterType)) {
            if (MediaType.TEXT_PLAIN.includes(selectedContentType)) {
                return o.textPlain();
            }
            if (MediaType.APPLICATION_JSON.includes(selectedContentType)
                    && Objects.nonNull(this.jacksonJsonHttpMessageConverter)) {
                return this.jacksonJsonHttpMessageConverter.getMapper().writeValueAsString(o);
            }
            if (MediaType.APPLICATION_XML.includes(selectedContentType)
                    && Objects.nonNull(this.jacksonXmlHttpMessageConverter)) {
                return this.jacksonXmlHttpMessageConverter.getMapper().writeValueAsString(o);
            }
        }
        return o;
    }

    private Object wrapForInternal(boolean exceptionHappen, Object body,
                                   Class<?> parameterType, ServerHttpResponse response) {
        HttpHeaders responseHeaders = response.getHeaders();

        if (body instanceof Result<?> result) {
            setHeaderIfAbsent(responseHeaders, RES_STATUS, String.valueOf(result.getStatus()));
            setHeaderIfAbsent(responseHeaders, RES_SUCCESS, String.valueOf(result.isSuccess()));
            setHeaderIfAbsent(responseHeaders, RES_CODE, result.getCode());
            setHeaderIfAbsent(responseHeaders, RES_MSG, URLEncoder.encode(result.getMsg(), StandardCharsets.UTF_8));
            List<String> errs = result.getErrs();
            if (CollectionUtils.isNotEmpty(errs)) {
                String errStr = JsonUtil.instance.toStr(errs);
                setHeaderIfAbsent(responseHeaders, RES_ERRS, URLEncoder.encode(errStr, StandardCharsets.UTF_8));
            }
            if (exceptionHappen)
                return result.getData();
            return result;
        }

        if (body instanceof ResCode resCode) {
            response.setStatusCode(HttpStatusCode.valueOf(resCode.getStatus()));
            setHeaderIfAbsent(responseHeaders, RES_STATUS, String.valueOf(resCode.getStatus()));
            setHeaderIfAbsent(responseHeaders, RES_SUCCESS, String.valueOf(resCode.isSuccess()));
            setHeaderIfAbsent(responseHeaders, RES_CODE, resCode.getCode());
            setHeaderIfAbsent(responseHeaders, RES_MSG, URLEncoder.encode(resCode.getMsg(), StandardCharsets.UTF_8));
            return resCode.create();
        }

        response.setStatusCode(HttpStatusCode.valueOf(200));
        setHeaderIfAbsent(responseHeaders, RES_STATUS, "200");
        setHeaderIfAbsent(responseHeaders, RES_SUCCESS, "true");
        setHeaderIfAbsent(responseHeaders, RES_CODE, "0");
        setHeaderIfAbsent(responseHeaders, RES_MSG, "OK");
        return body;
    }

    private static void setHeaderIfAbsent(HttpHeaders header, String key, String value) {
        if (Objects.isNull(header) || StringUtils.isAnyBlank(key, value))
            return;
        if (header.containsHeader(key))
            return;
        header.set(key, value);
    }

    private static Result<?> doWrap(Object body, ServerHttpResponse response) {
        if (body instanceof Result<?> result) {
            response.setStatusCode(HttpStatusCode.valueOf(result.getStatus()));
            return result;
        }
        if (body instanceof ResCode resCode) {
            response.setStatusCode(HttpStatusCode.valueOf(resCode.getStatus()));
            return resCode.create();
        }
        response.setStatusCode(HttpStatus.OK);
        return Res.OK.create(body);
    }

    @SuppressWarnings("RedundantIfStatement")
    private static boolean doSupports(MethodParameter returnType,
                                      Class<? extends HttpMessageConverter<?>> converterType,
                                      MediaType selectedContentType) {
        Method method = returnType.getMethod();
        if (Objects.isNull(method))
            return false;

        RequestMapping mapping = method.getAnnotation(RequestMapping.class);
        if (Objects.nonNull(mapping)) {
            String[] produces = mapping.produces();
            if (ArrayUtils.isNotEmpty(produces)) {
                for (String produce : produces) {
                    if (Objects.isNull(produce) || produce.trim().isEmpty())
                        continue;
                    if (produce.contains(MediaType.TEXT_EVENT_STREAM.getSubtype()))
                        return false;
                }
            }
        }

        if (method.isAnnotationPresent(RwIgnore.class))
            return false;
        if (method.isAnnotationPresent(Async.class))
            return false;
        if (exceptionHappen(returnType))
            return true;

        Class<?> parameterType = returnType.getParameterType();
        if (ResponseEntity.class.isAssignableFrom(parameterType))
            return false;
        if (StreamingResponseBody.class.isAssignableFrom(parameterType))
            return false;
        if (ResponseBodyEmitter.class.isAssignableFrom(parameterType))
            return false;
        if (Result.class.isAssignableFrom(parameterType))
            return false;
        if (ByteArrayHttpMessageConverter.class.isAssignableFrom(converterType))
            return false;
        if (BufferedImageHttpMessageConverter.class.isAssignableFrom(converterType))
            return false;
        if (ProtobufHttpMessageConverter.class.isAssignableFrom(converterType))
            return false;
        if (SourceHttpMessageConverter.class.isAssignableFrom(converterType))
            return false;
        if (StringHttpMessageConverter.class.isAssignableFrom(converterType)
                && MediaType.TEXT_PLAIN.includes(selectedContentType))
            return false;

        return true;
    }

    private static boolean exceptionHappen(MethodParameter returnType) {
        Method method = returnType.getMethod();
        if (Objects.isNull(method))
            return false;
        Class<?> declaringClass = method.getDeclaringClass();
        return GlobalExceptionAdvice.class.isAssignableFrom(declaringClass);
    }
}
