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

import com.asialjim.microapplet.common.cons.Headers;
import com.asialjim.microapplet.common.context.IORes;
import com.asialjim.microapplet.common.context.Res;
import com.asialjim.microapplet.common.context.Result;
import com.asialjim.microapplet.common.valid.ErrorInfo;
import com.asialjim.microapplet.common.valid.ResCodeValidationPayload;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * 全局错误增强
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/22, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionAdvice {
    private final ResCodeValidationPayload resCodeValidationPayload;

    /**
     * 处理丢失servlet请求参数异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        log.info("关键参数：{} 缺失异常", parameterName);
        return Res.ParameterEmptyEx.resultErrs(Collections.singletonList(parameterName));
    }

    /**
     * 处理方法参数类型不匹配异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String name = e.getName();
        log.info("关键参数：{} 类型异常", name);
        return Res.ParameterTypeEx.resultErrs(Collections.singletonList(name));
    }

    /**
     * 处理非法参数异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        log.info("关键参数：{} 非法", message);
        return Res.ParameterIllegalEx.resultErrs(Collections.singletonList(message));
    }

    /**
     * 处理非法参数异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<Void> handleIllegalArgumentException(BindException e) {
        List<ErrorInfo> errors = Optional.ofNullable(e)
                .map(BindException::getBindingResult)
                .map(this.resCodeValidationPayload::fromBindResult)
                .orElseGet(Collections::emptyList);

        return binExceptionResult(errors);
    }

    /**
     * 处理方法参数无效异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return handleIllegalArgumentException(e);
    }

    /**
     * 处理约束违反异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<ErrorInfo> errors = Optional.ofNullable(constraintViolations)
                .stream()
                .flatMap(Collection::stream)
                .map(this.resCodeValidationPayload::fromConstraintViolation)
                .toList();

        return binExceptionResult(errors);
    }

    /**
     * 处理超时异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(TimeoutException.class)
    public Result<Void> handleTimeoutException(TimeoutException e) {
        String message = e.getMessage();
        log.info("系统超时：{}", message);
        return IORes.TimeoutErr.resultErrs(Collections.singletonList(message));
    }

    /**
     * 处理超时异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SocketTimeoutException.class)
    public Result<Void> handleTimeoutException(SocketTimeoutException e) {
        String message = e.getMessage();
        log.info("三方系统网络超时：{}", message);
        return IORes.SocketTimeoutErr.resultErrs(Collections.singletonList(message));
    }

    /**
     * 处理IO异常
     *
     * @param e e
     * @return {@link Result<Void>}
     */
    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIOException(IOException e) {
        log.info("IO 异常：{}", e.getMessage());
        return IORes.IOErr.result();
    }

    /**
     * throwable
     *
     * @param e       e
     * @param request 请求
     * @return {@link Result<Void>}
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<Void> throwable(Throwable e, HttpServletRequest request) {
        e.printStackTrace();
        String logLevel = request.getHeader(Headers.HTTPLogLevel);
        List<Object> errs = new ArrayList<>();
        errs.add(e.getMessage());
        errs.add("\r\n");
        for (Throwable throwable : e.getSuppressed()) {
            errs.add(throwable.getMessage());
        }

        log.error("未明确类型错误：{} >>> {}", e.getMessage(), errs);
        if (StringUtils.equalsIgnoreCase(logLevel, "debug"))
            return Res.SysErr.resultErrs(errs);

        return Res.SysErr.result();
    }

    /**
     * 异常结果
     *
     * @param errors 错误
     * @return {@link Result<Void>}
     */
    private static Result<Void> binExceptionResult(List<ErrorInfo> errors) {
        log.info("参数验证错误：{}", errors);
        return Res.ParameterIllegalEx.resultErrs(errors);
    }
}