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

import com.asialjim.microapplet.common.cons.HttpHeaderCons;
import com.fasterxml.jackson.databind.deser.std.MapDeserializer;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

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

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String sessionId = request.getHeader(HttpHeaderCons.CloudSessionId);
        String traceId = request.getHeader(HttpHeaderCons.CloudTraceId);
        MDC.put(HttpHeaderCons.CloudSessionId,sessionId);
        MDC.put(HttpHeaderCons.CloudTraceId,traceId);
        filterChain.doFilter(request, response);
        MDC.clear();
    }
}