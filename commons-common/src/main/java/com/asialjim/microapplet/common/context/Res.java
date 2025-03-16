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

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 常见响应代码
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum Res implements ResCode {
    KeyParameterIllegal(false,"0005","关键参数异常"),
    KeyParameterTypeErr(false,"0004","关键参数类型不匹配"),
    KeyParameterEmptyErr(false,"0003","关键参数为空"),
    ValidParameterErr(false,"0002","参数校验失败"),
    IllegalParameter(false, "0001", "参数非法"),
    Ok(true, "0", "成功"),
    Fail(false, "-1", "失败"),
    SysBusy(false, "-2", "系统繁忙");

    private final boolean success;
    private final String code;
    private final String msg;
}