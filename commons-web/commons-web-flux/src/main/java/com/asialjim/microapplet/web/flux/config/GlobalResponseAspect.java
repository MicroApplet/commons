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

package com.asialjim.microapplet.web.flux.config;


import com.asialjim.microapplet.common.context.Res;
import com.asialjim.microapplet.common.context.Result;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.HandlerResult;
import org.springframework.web.server.ServerWebExchange;

import java.util.Map;
import java.util.Optional;

/**
 * 统一处理响应结果：数据封装
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Aspect
@Component
public class GlobalResponseAspect {
    private static final String SKIP_PARSE_RES_TO_RESULT = "_skip_parse_res_to_result_";

    public static void skip(Map<String, Object> ctx, Boolean skip) {
        ctx.put(SKIP_PARSE_RES_TO_RESULT, skip);
    }

    public static boolean skip(Map<String, Object> ctx) {
        return Optional.ofNullable(ctx)
                .map(item -> (Boolean) item.get(SKIP_PARSE_RES_TO_RESULT))
                .orElse(false);
    }


    @Around(value = "execution(reactor.core.publisher.Mono<java.lang.Void>" +
            " org.springframework.web.reactive.result.method.annotation.ResponseBodyResultHandler.handleResult( " +
            " org.springframework.web.server.ServerWebExchange," +
            " org.springframework.web.reactive.HandlerResult ))" +
            "&& args(exchange,result )",
            argNames = "joinPoint,exchange,result")
    public Object aroundHandleResult(ProceedingJoinPoint joinPoint, ServerWebExchange exchange, HandlerResult result) throws Throwable {
        final Object[] targetArgs = new Object[2];
        targetArgs[0] = exchange;
        targetArgs[1] = result;

        Object returnValue = result.getReturnValue();

        Boolean skip = (skip(exchange.getAttributes()) || (returnValue instanceof Result));
        if (!Boolean.TRUE.equals(skip)) {
            Result<Object> res = Res.Ok.create(returnValue);
            HandlerResult handlerResult =
                    new HandlerResult(result.getHandler(),
                            res,
                            result.getReturnTypeSource(),
                            result.getBindingContext());

            targetArgs[1] = handlerResult;
        }
        return joinPoint.proceed(targetArgs);
    }
}