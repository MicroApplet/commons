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

package com.asialjim.microapplet.web.mvc.auth;

import com.asialjim.microapplet.session.SessionRepository;
import com.asialjim.microapplet.session.Session;
import com.asialjim.microapplet.session.SessionCtx;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
import java.util.Set;

/**
 * 基于 WebMVC 的会话上下文实现
 * <p>从当前 HTTP 请求中提取令牌，通过 {@link SessionRepository} 查询会话。</p>
 * <p>同一请求内多次调用走 request attribute 缓存，不重复查库。</p>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Component
@AllArgsConstructor
public class MvcSessionCtx implements SessionCtx {
    private static final String SESSION_ATTR = MvcSessionCtx.class.getName() + ".SESSION";

    private final SessionRepository sessionRepository;

    @Override
    public SessionRepository sessionRepository() {
        return this.sessionRepository;
    }

    @Override
    public Session currentSession() {
        HttpServletRequest request = currentRequest();
        if (Objects.isNull(request))
            return null;

        // 同请求内缓存
        Session cached = (Session) request.getAttribute(SESSION_ATTR);
        if (Objects.nonNull(cached))
            return cached;

        String token = extractToken(request);
        if (StringUtils.isBlank(token))
            return null;

        Session session = sessionRepository.findByToken(token);
        if (Objects.nonNull(session))
            request.setAttribute(SESSION_ATTR, session);

        return session;
    }

    @Override
    public void login(Session session) {
        HttpServletRequest request = currentRequest();
        if (Objects.isNull(request))
            return ;
        request.setAttribute(SESSION_ATTR,session);
    }


    @Override
    public Session auth(Set<String> tokens) {
        if (Objects.isNull(tokens) || tokens.isEmpty())
            return null;

        for (String token : tokens) {
            String t = token.replaceFirst("Bearer ", StringUtils.EMPTY).trim();
            if (StringUtils.isBlank(t))
                continue;

            Session session = sessionRepository.findByToken(t);
            if (Objects.nonNull(session))
                return session;
        }
        return null;
    }

    @Override
    public void save(Session session) {
        this.sessionRepository.save(session);
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }

    private static HttpServletRequest currentRequest() {
        var attrs = RequestContextHolder.getRequestAttributes();
        if (attrs instanceof ServletRequestAttributes sra)
            return sra.getRequest();
        return null;
    }

    private static String extractToken(HttpServletRequest request) {
        for (String header : TOKENS) {
            String value = request.getHeader(header);
            if (StringUtils.isNotBlank(value))
                return value.replaceFirst("Bearer ", StringUtils.EMPTY).trim();
        }

        Cookie[] cookies = request.getCookies();
        if (Objects.nonNull(cookies)) {
            for (Cookie cookie : cookies) {
                if ("X-User-Token".equals(cookie.getName()) && StringUtils.isNotBlank(cookie.getValue()))
                    return cookie.getValue();
            }
        }

        return null;
    }
}
