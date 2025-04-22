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

import com.asialjim.microapplet.common.cons.HttpHeaderCons;
import com.asialjim.microapplet.common.context.IORes;
import com.asialjim.microapplet.common.context.Res;
import com.asialjim.microapplet.common.context.Result;
import com.asialjim.microapplet.common.exception.BusinessException;
import com.asialjim.microapplet.common.exception.SystemException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBizException(BusinessException e) {
        log.info("业务错误：{}", e.toString());
        int status = e.getStatus();
        Result<Void> objectResult = e.create();
        HttpStatus resolve = Optional.ofNullable( HttpStatus.resolve(status)).orElse(HttpStatus.OK);
        return new ResponseEntity<>(objectResult, resolve);
    }

    @ExceptionHandler(SystemException.class)
    public ResponseEntity<Result<Void>> handleSysException(SystemException e) {
        log.info("系统错误：{}", e.toString());
        int status = e.getStatus();
        Result<Void> objectResult = e.create();
        HttpStatus resolve = Optional.ofNullable( HttpStatus.resolve(status)).orElse(HttpStatus.OK);
        return new ResponseEntity<>(objectResult, resolve);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        String parameterName = e.getParameterName();
        log.info("关键参数：{} 缺失异常", parameterName);
        return Res.KeyParameterEmptyErr.create(Collections.singletonList(parameterName));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Void> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String name = e.getName();
        log.info("关键参数：{} 类型异常", name);
        return Res.KeyParameterTypeErr.create(Collections.singletonList(name));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgumentException(IllegalArgumentException e) {
        String message = e.getMessage();
        log.info("关键参数：{} 非法", message);
        return Res.KeyParameterIllegal.create(Collections.singletonList(message));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Result<Void> handleIllegalArgumentException(BindException e) {
        List<Object> errors = Optional.ofNullable(e)
                .map(BindException::getBindingResult)
                .map(Errors::getAllErrors)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());
        log.info("参数验证错误：{}", errors);
        return Res.KeyParameterIllegal.create(errors);
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(TimeoutException.class)
    public Result<Void> handleTimeoutException(TimeoutException e) {
        String message = e.getMessage();
        log.info("系统超时：{}", message);
        return IORes.TimeoutErr.create(Collections.singletonList(message));
    }

    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    @ExceptionHandler(SocketTimeoutException.class)
    public Result<Void> handleTimeoutException(SocketTimeoutException e) {
        String message = e.getMessage();
        log.info("三方系统网络超时：{}", message);
        return IORes.SocketTimeoutErr.create(Collections.singletonList(message));
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result<Void> handleIOException(IOException e) {
        log.info("IO 异常：{}", e.getMessage());
        return IORes.IOErr.create();
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public Result<Void> throwable(Throwable e, HttpServletRequest request) {
        String logLevel = request.getHeader(HttpHeaderCons.HTTP_LOG_LEVEL);
        if (StringUtils.equalsIgnoreCase(logLevel, "debug")) {
            List<Object> errs = new ArrayList<>();
            errs.add(e.getMessage());
            errs.add("\r\n");
            for (Throwable throwable : e.getSuppressed()) {
                errs.add(throwable.getMessage());
            }

            e.printStackTrace();
            log.error("未明确类型错误：{}", e.getMessage());
            return Res.SysBusy.create(errs);
        }

        log.error("未明确类型错误：{}", e.getMessage());
        return Res.SysBusy.create();
    }
}