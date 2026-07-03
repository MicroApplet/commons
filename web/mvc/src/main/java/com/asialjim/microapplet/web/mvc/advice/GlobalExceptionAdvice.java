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

import com.asialjim.microapplet.commons.standard.context.Res;
import com.asialjim.microapplet.commons.standard.context.Result;
import com.asialjim.microapplet.commons.standard.exception.BusinessException;
import com.asialjim.microapplet.commons.standard.exception.RsEx;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeoutException;

/**
 * MVC 全局统一异常处理
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@RestControllerAdvice(basePackages = "com.asialjim.microapplet")
public class GlobalExceptionAdvice {

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handleMissingServletRequestParameterEx(MissingServletRequestParameterException e) {
        return Res.ParameterEmptyEx.ex(Collections.singletonList(e.getParameterName())).result();
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handleMethodArgumentTypeMismatchEx(MethodArgumentTypeMismatchException e) {
        return Res.ParameterTypeEx.ex(Collections.singletonList(e.getName())).result();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handleIllegalArgumentEx(IllegalArgumentException e) {
        return Res.ParameterIllegalEx.ex(Collections.singletonList(e.getMessage())).result();
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handleBindException(BindException e) {
        List<String> messages = Optional.ofNullable(e)
                .map(BindException::getBindingResult)
                .map(this::fromBindResult)
                .stream()
                .flatMap(Collection::stream)
                .toList();
        return Res.ParameterIllegalEx.ex(messages).result();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleMethodArgumentNotValidEx(MethodArgumentNotValidException e) {
        return handleBindException(e);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<?> handleConstrainViolationEx(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        List<String> messages = Optional.ofNullable(constraintViolations)
                .stream()
                .flatMap(Collection::stream)
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .toList();
        return Res.ParameterIllegalEx.ex(messages).result();
    }

    @ExceptionHandler(TimeoutException.class)
    public Result<?> handleTimeoutEx(TimeoutException e) {
        return Res.SysErr.ex(Collections.singletonList(e.getMessage())).result();
    }

    @ExceptionHandler(SocketTimeoutException.class)
    public Result<?> handleSocketTimeout(SocketTimeoutException e) {
        return Res.SysErr.ex(Collections.singletonList(e.getMessage())).result();
    }

    @ExceptionHandler(IOException.class)
    public Result<?> handleIOException(IOException e) {
        return Res.SysErr.ex(Collections.singletonList(e.getMessage())).result();
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<?> handleHttpMessageNotReadableEx(HttpMessageNotReadableException e) {
        return Res.ParameterEmptyEx.ex(List.of("空请求体", e.getMessage())).result();
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public Result<?> handleNoHandlerFoundEx(NoHandlerFoundException e) {
        return Res._404.ex(Collections.singletonList(e.getMessage())).result();
    }

    @ExceptionHandler(RsEx.class)
    public Result<?> handleThrowable(RsEx e) {
        return e.result();
    }
    @ExceptionHandler(BusinessException.class)
    public Result<?> handleThrowable(BusinessException e) {
        return e.create();
    }

    @ExceptionHandler(Throwable.class)
    public Result<?> handleThrowable(Throwable e) {
        log.error("未知错误异常:{} - {}", e.getClass(), e.getMessage(),e);
        return Res.SysErr.ex(Collections.singletonList(e.getMessage())).result();
    }

    private List<String> fromBindResult(BindingResult result) {
        List<FieldError> fieldErrors = result.getFieldErrors();
        if (CollectionUtils.isEmpty(fieldErrors))
            return List.of(Res.ParameterValidEx.getCode() + ": " + result.getObjectName());

        return fieldErrors.stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
    }
}
