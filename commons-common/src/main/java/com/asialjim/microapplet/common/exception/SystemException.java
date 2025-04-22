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

package com.asialjim.microapplet.common.exception;

import com.asialjim.microapplet.common.context.ResCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 系统异常
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public class SystemException extends RuntimeException implements ResCode {
    private final int status;
    private final String code;
    private final String msg;
    private final String trace;


    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public String toString() {
        return "SystemException:Code=" + this.code + ",Message=" + this.msg + ",Trace=" +this.getTrace();
    }
}