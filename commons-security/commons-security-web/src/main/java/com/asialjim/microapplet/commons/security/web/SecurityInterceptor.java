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
import com.asialjim.microapplet.commons.security.Role;
import com.asialjim.microapplet.commons.security.RoleNeed;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

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
        if (!(handler instanceof HandlerMethod handlerMethod))
            return true;

        // 非静态资源
        Method method = handlerMethod.getMethod();
        Class<?> beanType = handlerMethod.getBeanType();

        // 检测方法是否存在指定注解
        if (touristNeed(method.getAnnotation(RoleNeed.class)))
            return true;

        if (touristNeed(beanType.getAnnotation(RoleNeed.class)))
            return true;

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

    private static boolean touristNeed(RoleNeed annotation) {
        if (Objects.isNull(annotation))
            return false;
        Role[] any = annotation.any();

        AtomicLong atomicLong = new AtomicLong(0);
        Arrays.stream(any).map(Role::getBit).forEach(atomicLong::addAndGet);
        long role = atomicLong.get();

        // 游客就能访问，不需要登录
        if (Role.Tourist.is(role))
            return true;
        return false;
    }
}