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
    SysErr(500,true,"SYS:BUSINESS","系统繁忙，请稍后再试"),
    ParameterIllegalEx(400,true,"PARAM:ILLEGAL:EX","非法参数异常"),
    ParameterEmptyEx(400,true,"PARAM:EMPTY:EX","关键参数为空"),
    ParameterTypeEx(400,true,"PARAM:TYPE:EX","参数类型不匹配"),
    ParameterValidEx(400,true,"PARAM:VALID:EX","参数校验异常"),

    UserAuthFailure401Thr(401, true, "USER:AUTH:EX", "请登录"),
    UserAuthFailure401(401, "USER:AUTH:EX", "请登录"),
    UserAuthFailureThr(true, "USER:AUTH:EX", "请登录"),
    UsernameOfPasswordIllegal(true, "USER:AUTH:EX", "用户名不存在或者密码错误"),

    UserWithoutRole(400,true,"USER:ROLE:WITHOUT","用户没有以下角色"),
    UserRoleNeedAtLeast(400,true,"USER:ROLE:NEED-AT-LEAST","用户至少需要以下其中一个角色"),

    OK("0", "成功"),
    Ok("0", "成功"),
    Success("0", "成功");

    private final int status;
    private final boolean thr;
    private final String code;
    private final String msg;


    /**
     * res
     *
     * @param status 状态
     * @param code   代码
     * @param msg    味精
     */
    Res(int status, String code, String msg) {
        this(status, false, code, msg);
    }

    /**
     * res
     *
     * @param thr  用力推
     * @param code 代码
     * @param msg  味精
     */
    Res(boolean thr, String code, String msg) {
        this(200, thr, code, msg);
    }

    /**
     * res
     *
     * @param code 代码
     * @param msg  味精
     */
    Res(String code, String msg) {
        this(200, false, code, msg);
    }

}