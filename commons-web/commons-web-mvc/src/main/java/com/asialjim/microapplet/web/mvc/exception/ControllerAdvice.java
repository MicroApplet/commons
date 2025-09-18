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

package com.asialjim.microapplet.web.mvc.exception;

import com.asialjim.microapplet.common.cons.Headers;
import com.asialjim.microapplet.common.context.IORes;
import com.asialjim.microapplet.common.context.Res;
import com.asialjim.microapplet.common.context.Result;
import com.asialjim.microapplet.common.exception.RsEx;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

/**
 * 基础错误增强
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        log.info("关键参数：{} 缺失异常", parameterName);
        return Res.ParameterEmptyEx.resultErrs(Collections.singletonList(parameterName));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String name = e.getName();
        log.info("关键参数：{} 类型异常", name);
        return Res.ParameterTypeEx.resultErrs(Collections.singletonList(name));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        log.info("关键参数：{} 非法", message);
        return Res.ParameterIllegalEx.resultErrs(Collections.singletonList(message));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<Void> handleIllegalArgumentException(BindException e) {
        List<Object> errors = Optional.ofNullable(e)
                .map(BindException::getBindingResult)
                .map(Errors::getAllErrors)
                .stream()
                .flatMap(Collection::stream)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        log.info("参数验证错误：{}", errors);
        return Res.ParameterIllegalEx.resultErrs(errors);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(TimeoutException.class)
    public Result<Void> handleTimeoutException(TimeoutException e) {
        String message = e.getMessage();
        log.info("系统超时：{}", message);
        return IORes.TimeoutErr.resultErrs(Collections.singletonList(message));
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SocketTimeoutException.class)
    public Result<Void> handleTimeoutException(SocketTimeoutException e) {
        String message = e.getMessage();
        log.info("三方系统网络超时：{}", message);
        return IORes.SocketTimeoutErr.resultErrs(Collections.singletonList(message));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIOException(IOException e) {
        log.info("IO 异常：{}", e.getMessage());
        return IORes.IOErr.result();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<Void> throwable(Throwable e, HttpServletRequest request) {
        String logLevel = request.getHeader(Headers.HTTPLogLevel);
        List<Object> errs = new ArrayList<>();
        errs.add(e.getMessage());
        errs.add("\r\n");
        for (Throwable throwable : e.getSuppressed()) {
            errs.add(throwable.getMessage());
        }

        log.error("未明确类型错误：{} >>> {}", e.getMessage(), errs);
        if (StringUtils.equalsIgnoreCase(logLevel, "debug")) {
            return Res.SysErr.resultErrs(errs);
        }

        return Res.SysErr.result();
    }


    @ExceptionHandler(RsEx.class)
    public Result<?> handle(RsEx ex) {
        return ex.result();
    }

    @ExceptionHandler(BindException.class)
    public Result<?> handle(BindException ex) {
        List<String> errs = Optional.ofNullable(ex)
                .map(BindException::getBindingResult)
                .map(Errors::getAllErrors)
                .stream()
                .flatMap(Collection::stream)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .toList();

        return Res.ParameterValidEx.resultErrs(errs);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<?> handle(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        return Res.ParameterEmptyEx.resultErrs(Collections.singletonList(parameterName));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<?> handle(MethodArgumentTypeMismatchException ex) {
        String parameterName = ex.getParameter().getParameterName();
        return Res.ParameterTypeEx.resultErrs(Collections.singletonList(parameterName));
    }

    @ExceptionHandler(Exception.class)
    public Result<?> handle(Exception ex) {
        String message = ex.getMessage();
        return Res.SysErr.resultErrs(Collections.singletonList(message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<?> handle(IllegalArgumentException ex) {
        return Res.ParameterIllegalEx.resultErrs(Collections.singletonList(ex.getMessage()));
    }
}