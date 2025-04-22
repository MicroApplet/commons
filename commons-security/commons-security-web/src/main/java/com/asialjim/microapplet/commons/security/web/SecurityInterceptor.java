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

package com.asialjim.microapplet.commons.security.web;

import com.asialjim.microapplet.common.exception.UnLoginException;
import com.asialjim.microapplet.commons.security.AuthorityRes;
import com.asialjim.microapplet.commons.security.HasRole;
import com.asialjim.microapplet.commons.security.Tourist;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;

/**
 * 安全拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
@AllArgsConstructor
public class SecurityInterceptor implements HandlerInterceptor {
    private final SessionSecurityManager sessionSecurityManager;

    @Override
    public boolean preHandle(@SuppressWarnings("NullableProblems") HttpServletRequest request,
                             @SuppressWarnings("NullableProblems") HttpServletResponse response,
                             @SuppressWarnings("NullableProblems") Object handler) throws IOException {

        // 静态资源
        if (!(handler instanceof HandlerMethod))
            return true;

        // 非静态资源
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检测方法是否存在指定注解
        if (method.isAnnotationPresent(HasRole.class)) {
            HasRole annotation = method.getAnnotation(HasRole.class);
            // 游客就能访问，不需要登录
            if (Arrays.stream(annotation.value()).anyMatch(item -> Objects.equals(item, Tourist.code)))
                return true;
        }

        // 其他情况需要登录
        try {
            sessionSecurityManager.loginCheck(request);
            return true;
        } catch (UnLoginException e) {
            String res = AuthorityRes.NoSignIn.create().toString();
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(res);
            return false;
        }
    }
}