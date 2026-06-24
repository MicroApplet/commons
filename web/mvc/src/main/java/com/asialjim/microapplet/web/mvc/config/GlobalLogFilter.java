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

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.util.*;
import static com.asialjim.microapplet.web.client.MamsHttpHeaders.*;

/**
 * 基于 webmvc 的全局日志处理器，用于链路追踪，请求、响应日志搜集
 *
 * @author Asial Jim
 * @version 1.0
 * @since 2026/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Component
@Order(-100)
public class GlobalLogFilter implements Filter {
    private static final List<String> expectedHeaders =
            List.of(SESSION_ID,
                    TRACE_ID,
                    PLATFORM_TYPE,
                    APP_ID,
                    APP_TYPE,
                    ENC_KEY_VERSION,
                    HttpHeaders.USER_AGENT,
                    HttpHeaders.ACCEPT
            );


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            HttpServletResponse response = (HttpServletResponse) servletResponse;

            String sessionId = Optional.ofNullable(request.getHeader(SESSION_ID)).filter(StringUtils::isNotBlank).orElse("NO-SESSION");
            String traceId = Optional.ofNullable(request.getHeader(TRACE_ID)).filter(StringUtils::isNotBlank).orElse(UUID.randomUUID().toString().replace("-", ""));

            MDC.put(SESSION_ID, sessionId);
            MDC.put(TRACE_ID, traceId);

            logRequestHeader(request);
            filterChain.doFilter(request, response);
            logResponseHeader(response);

        } finally {
            MDC.clear();
        }
    }

    private void logResponseHeader(HttpServletResponse response) {
        Collection<String> headerNames = response.getHeaderNames();
        StringJoiner j = new StringJoiner("\r\n\t");
        j.add(StringUtils.EMPTY);
        Set<String> hadLog = new HashSet<>();
        for (String name : headerNames) {
            if (!hadLog.contains(name)) {
                j.add(name + "=" + response.getHeader(name));
                hadLog.add(name);
            }
        }
        log.info("\r\n<<响应头: {}", j);
    }

    private void logRequestHeader(HttpServletRequest request) {
        StringJoiner j = new StringJoiner("\r\n");
        String method = request.getMethod();
        StringBuffer requestURL = request.getRequestURL();
        j.add(StringUtils.EMPTY);
        j.add(">>请求行: [" + method + "] " + requestURL);

        StringJoiner headerJ = new StringJoiner("\r\n\t");
        headerJ.add(StringUtils.EMPTY);
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            // 只筛选感兴趣的Header，避免日志膨胀
            if (log.isDebugEnabled()) {
                String value = request.getHeader(name);
                headerJ.add(name + "=" + value);
            } else {
                if (expectedHeaders.stream().anyMatch(item -> Strings.CI.equals(item, name))) {
                    String value = request.getHeader(name);
                    headerJ.add(name + "=" + value);
                }
            }
        }
        // 使用安全的日志记录方式对请求头进行处理防止日志诸如攻击
        j.add(">>请求头: " + HtmlUtils.htmlEscape(headerJ.toString()));
        log.info(j.toString());
    }
}
