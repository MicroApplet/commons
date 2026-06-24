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

import com.asialjim.microapplet.web.mvc.annotation.LogIgnore;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;

/**
 * 全局 Controller 日志处理器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@Aspect
@Component
public class GlobalControllerAdvice {

    @Around("""
            @within(org.springframework.web.bind.annotation.RestController)
                        ||
                        @within(org.springframework.stereotype.Controller)
            """)
    public Object advice(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String typeName = signature.getDeclaringType().getSimpleName();
        Method method = signature.getMethod();
        if (!method.isAnnotationPresent(LogIgnore.class))
            return joinPoint.proceed();

        String methodName = method.getName();
        final String handler = typeName + "." + methodName;

        StringJoiner logJ = new StringJoiner("\r\n");
        logJ.add(StringUtils.EMPTY);

        Object proceed;
        final StopWatch stopWatch = new StopWatch();
        try {
            logJ.add(">>处理器：" + handler + " 参数表：" + List.of(joinPoint.getArgs()));
            stopWatch.start();
            proceed = joinPoint.proceed();
            stopWatch.stop();
            return proceed;
        } catch (Throwable t) {
            stopWatch.stop();
            if (log.isDebugEnabled())
                log.debug("XX处理器：{}执行异常：{}", handler, t.getMessage(), t);
            logJ.add("XX处理器：" + handler + " 执行异常：" + t.getMessage());
            throw t;
        } finally {
            long time = stopWatch.getTime(TimeUnit.MILLISECONDS);
            logJ.add("==处理器：" + handler + " 执行完成，耗时：" + time + " 毫秒");
            if (Objects.nonNull(proceed = joinPoint.proceed()))
                logJ.add("<<结  果：" + proceed);
            log.info(logJ.toString());
        }
    }
}
