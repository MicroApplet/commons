/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.common.context;

import com.asialjim.microapplet.common.exception.BusinessException;
import com.asialjim.microapplet.common.exception.SystemException;

import java.util.Collections;
import java.util.List;

/**
 * 通用响应代码
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface ResCode {
    boolean isSuccess();

    default int getStatus(){return 200;}

    /**
     * 响应代码
     */
    String getCode();

    /**
     * 响应消息
     */
    String getMsg();

    default String getTrace() {
        return "";
    }

    default <T> Result<T> create() {
        return create(null, Collections.emptyList());
    }

    default <T> Result<T> create(T data) {
        return create(data, Collections.emptyList());
    }

    default <T> Result<T> create(List<Object> errs) {
        return create(null, errs);
    }

    default <T> Result<T> create(T data, List<Object> errs) {
        return create(getMsg(), data, errs);
    }

    default <T> Result<T> create(String msg, T data, List<Object> errs) {
        return create(getCode(), msg, data, errs);
    }

    default <T> Result<T> create(String code, String msg, T data, List<Object> errs) {
        return create(isSuccess(), code, msg, data, errs);
    }

    default <T> Result<T> create(boolean success, String code, String msg, T data, List<Object> errs) {
        return new Result<T>().setStatus(getStatus()).setSuccess(success).setCode(code).setMsg(msg).setData(data).setErrs(errs);
    }
    default <T> Result<T> create(boolean success, int status, String code, String msg, T data, List<Object> errs) {
        return new Result<T>().setStatus(status).setSuccess(success).setCode(code).setMsg(msg).setData(data).setErrs(errs);
    }

    default BusinessException bizException() {
        return new BusinessException(getStatus(),getCode(), getMsg());
    }

    default SystemException sysException() {
        return new SystemException(getStatus(),getCode(), getMsg(), getTrace());
    }

    default SystemException sysException(String trace) {
        return new SystemException(getStatus(),getCode(), getMsg(), trace);
    }

    default void throwBiz() {
        throw bizException();
    }

    default void throwSys() {
        throw sysException();
    }
    default void throwSys(String trace) {
        throw sysException(trace);
    }
}