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

package com.asialjim.microapplet.web.mvc.config;

import com.asialjim.microapplet.common.cons.Headers;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collection;
import java.util.StringJoiner;

/**
 * 全局链路追踪组件
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/5/6, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Component
public class GlobalLogFilter implements Filter {

    /**
     * 做的过滤器
     *
     * @param servletRequest  servlet请求
     * @param servletResponse servlet响应
     * @param filterChain     过滤器链
     * @throws IOException      无效
     * @throws ServletException 无效
     */
    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            String sessionId = request.getHeader(Headers.SessionId);
            String traceId = request.getHeader(Headers.TraceId);
            MDC.put(Headers.SessionId, sessionId);
            MDC.put(Headers.TraceId, traceId);
            filterChain.doFilter(request, response);
            Collection<String> responseHeaders = response.getHeaderNames();
            final StringJoiner headerJ = new StringJoiner("\r\n\t");
            headerJ.add(StringUtils.EMPTY);
            responseHeaders.forEach(name -> headerJ.add(name + "=" + response.getHeader(name)));
            log.info("\r\n<<响应头: {}", headerJ);
        } finally {
            MDC.clear();
        }
    }
}