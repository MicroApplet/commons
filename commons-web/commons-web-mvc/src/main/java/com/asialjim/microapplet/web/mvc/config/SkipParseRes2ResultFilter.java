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

import com.asialjim.microapplet.web.mvc.advice.GlobalResponseAspect;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.stereotype.Component;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * WebMVC 下，将 HTTP Header 存储到 Reactor.context 中
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/4, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class SkipParseRes2ResultFilter implements Filter {
    private ServerProperties serverProperties;
    private List<SkipParseRes2ResultConfig> skipParseRes2ResultConfigList;

    @Autowired(required = false)
    public void setServerProperties(ServerProperties serverProperties) {
        this.serverProperties = serverProperties;
    }

    @Autowired(required = false)
    public void setSkipParseRes2ResultConfigList(List<SkipParseRes2ResultConfig> skipParseRes2ResultConfigList) {
        this.skipParseRes2ResultConfigList = skipParseRes2ResultConfigList;
    }

    private String contextPath() {
        return Optional.ofNullable(this.serverProperties)
                .map(ServerProperties::getServlet)
                .map(ServerProperties.Servlet::getContextPath)
                .orElse(StringUtils.EMPTY);
    }

    private Stream<SkipParseRes2ResultConfig> skipFunctions() {
        return Optional.ofNullable(this.skipParseRes2ResultConfigList).map(Collection::stream).orElseGet(Stream::empty);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String asciiString = httpRequest.getRequestURI();
        String context = contextPath();

        boolean skip = skipFunctions().anyMatch(item -> item.skipParseRes2ResultCheck(asciiString, context));
        GlobalResponseAspect.skip(httpRequest, skip, asciiString);
        filterChain.doFilter(httpRequest, httpResponse);
        GlobalResponseAspect.clean(asciiString);
    }
}