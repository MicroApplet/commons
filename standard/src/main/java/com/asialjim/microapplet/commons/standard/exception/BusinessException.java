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

package com.asialjim.microapplet.commons.standard.exception;

import com.asialjim.microapplet.commons.standard.context.ResCode;
import com.asialjim.microapplet.commons.standard.context.Result;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * 业务异常
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@ToString
@AllArgsConstructor
public class BusinessException extends RuntimeException implements ResCode {
    private final int status;
    private final String code;
    private final String msg;
    private final Object data;
    private final List<String> errs;


    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public boolean isThr() {
        return true;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    /**
     * 创建
     *
     * @return {@link Result <T>}
     */
    @SuppressWarnings("unchecked")
    public Result<Object> create() {
        return this.result(this.data, this.errs);
    }

    public String detailMessage(){
        return "API异常，网络状态码：%d, 业务状态码：%s, 错误信息：%s,详细错误信息:%s".formatted(this.status,this.code,this.msg,String.join(",",this.errs));
    }
}