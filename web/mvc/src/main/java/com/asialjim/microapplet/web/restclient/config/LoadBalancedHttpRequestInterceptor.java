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

package com.asialjim.microapplet.web.restclient.config;

import com.asialjim.microapplet.commons.standard.exception.BusinessException;
import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import com.asialjim.microapplet.commons.standard.utils.XmlUtil;
import com.asialjim.microapplet.web.restclient.CachedClientHttpResponse;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;

import static com.asialjim.microapplet.web.client.MamsHttpHeaders.*;

public class LoadBalancedHttpRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    @SuppressWarnings("NullableProblems")
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        //  处理链路追踪
        //  向下传播会话编号, 下游服务可通过会话编号获取用户会话信息

        String requestId = MDC.get(TRACE_ID);
        String sessionId = MDC.get(SESSION_ID);
        String token = MDC.get(USER_TOKEN_KEY);

        HttpRequestWrapper reqWrapper = new HttpRequestWrapper(request) {

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.putAll(super.getHeaders());
                headers.set(HTTP_CLIENT_TYPE, LOAD_BALANCE_CLIENT);
                if (StringUtils.isNotBlank(sessionId))
                    headers.set(SESSION_ID, sessionId);
                if (StringUtils.isNotBlank(requestId))
                    headers.set(TRACE_ID, requestId);
                if (StringUtils.isNotBlank(token))
                    headers.set(USER_TOKEN_KEY, token);
                return headers;
            }
        };


        // 实际调用
        CachedClientHttpResponse response = new CachedClientHttpResponse(execution.execute(reqWrapper, body));

        HttpHeaders headers = response.getHeaders();
        byte[] resBuffer = response.buffer();

        System.out.println("Header: ");
        System.out.println(headers);

        String success = headerStr(headers, RES_SUCCESS);
        // 远程调用业务成功
        if (Boolean.parseBoolean(success)) {
            return response;
        }

        String code = headerStr(headers, RES_CODE);
        if (StringUtils.isNotBlank(code))
            code = URLDecoder.decode(code, StandardCharsets.UTF_8);

        String msg = headerStr(headers, RES_MSG);
        if (StringUtils.isNotBlank(msg))
            msg = URLDecoder.decode(msg, StandardCharsets.UTF_8);

        String errsStr = headerStr(headers, RES_ERRS);
        List<String> errs;
        if (StringUtils.isNotBlank(errsStr)) {
            String decode = URLDecoder.decode(errsStr, StandardCharsets.UTF_8);
            errs = JsonUtil.instance.toList(decode, String.class);
        } else {
            errs = null;
        }

        MediaType contentType = response.getHeaders().getContentType();
        Object res = null;
        if (MediaType.APPLICATION_JSON.equals(contentType)) {
            res = JsonUtil.instance.toTree(resBuffer);
        } else if (MediaType.APPLICATION_XML.equals(contentType)) {
            res = XmlUtil.instance.toTree(resBuffer);
        } else {
            if (ArrayUtils.isNotEmpty(resBuffer))
                res = new String(resBuffer);
        }


        throw new BusinessException(200, code, msg, res, errs);
    }


    static String headerStr(HttpHeaders headers, String key) {
        if (StringUtils.isBlank(key))
            return StringUtils.EMPTY;

        if (Objects.isNull(headers) || headers.isEmpty())
            return StringUtils.EMPTY;

        List<String> strings = headers.get(key);

        if (CollectionUtils.isEmpty(strings))
            return StringUtils.EMPTY;

        for (String string : strings) {
            if (StringUtils.isNotBlank(string))
                return string;
        }
        return StringUtils.EMPTY;
    }
}