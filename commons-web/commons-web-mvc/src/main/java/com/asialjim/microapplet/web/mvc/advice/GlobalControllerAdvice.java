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

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.Enumeration;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 全局响应结果包装通知
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Aspect
@Component
public class GlobalControllerAdvice {

    @Around(value = "@within(org.springframework.web.bind.annotation.RestController) || @within(org.springframework.stereotype.Controller)")
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        final StringJoiner logJ = new StringJoiner("\r\n");
        logJ.add(StringUtils.EMPTY);

        final ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            final HttpServletRequest request = requestAttributes.getRequest();

            final String method = request.getMethod();
            final String requestURI = request.getRequestURI();
            logJ.add(">>请求行: [" + method + "] " + requestURI);

            final StringJoiner headerJ = new StringJoiner("\r\n\t");
            headerJ.add(StringUtils.EMPTY);
            Enumeration<String> headerNames = request.getHeaderNames();
            while (headerNames.hasMoreElements()) {
                String name = headerNames.nextElement();
                String value = request.getHeader(name);
                headerJ.add(name + "=" + value);
            }
            logJ.add(">>请求头: " + headerJ);
        }

        final Object[] args = joinPoint.getArgs();
        if (ArrayUtils.isNotEmpty(args))
            logJ.add(">>参  数: " + Arrays.asList(args));

        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final String typeName = signature.getDeclaringType().getSimpleName();
        final String methodName = signature.getMethod().getName();
        final String handler = typeName + "." + methodName;

        Object proceed = null;
        final StopWatch stopWatch = new StopWatch();
        try {
            stopWatch.start();
            proceed = joinPoint.proceed();
            return proceed;
        } catch (Throwable throwable) {
            if (log.isDebugEnabled())
                log.debug("\r\nXX处理器：{} 执行异常：{}", handler, throwable.getMessage(), throwable);
            logJ.add("\r\nXX处理器：" + handler + " 执行异常：" + throwable.getMessage());
            throw throwable;
        } finally {
            stopWatch.stop();
            final long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            logJ.add("==处理器：" + handler + " 耗时：" + time + " 毫秒");
            if (Objects.nonNull(proceed))
                logJ.add("<<结  果: " + proceed);
            log.info(logJ.toString());
        }
    }
}