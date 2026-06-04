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
import com.asialjim.microapplet.common.context.Result;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 业务异常
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/22, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class RsEx extends RuntimeException implements ResCode {
    private int status;
    private boolean thr;
    private String code;
    private String msg;
    private List<String> errs;

    @Override
    public String getMessage() {
        return "RsEx, Status: " + status + ", Throwable: " + thr + ", Code: " + code + ", Msg: " + msg + ", Errs: " + errs;
    }


    public void cast(){
        throw this;
    }

    @Override
    public Result<Void> result() {
        return new Result<Void>()
                .setStatus(getStatus())
                .setPageable(false)
                .setThr(isThr())
                .setCode(getCode())
                .setMsg(getMsg())
                .setData(null)
                .setErrs(errs);
    }
}