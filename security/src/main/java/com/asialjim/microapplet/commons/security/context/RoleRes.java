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

package com.asialjim.microapplet.commons.security.context;

import com.asialjim.microapplet.commons.standard.context.ResCode;
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
public enum RoleRes implements ResCode {
    SessionNeedRole(403,true,"USER:ROLE:NEED-ANY","用户需要以下角色"),
    SessionNeedRolesAny(403,true,"USER:ROLE:NEED-ANY","用户需要以下任意角色"),
    SessionNeedRolesAll(403,true,"USER:ROLE:NEED-ALL","用户需要以下所有角色"),
    RoleNotExist(500,true,"SYSTEM:ROLE:NOT-EXIST","系统没有该角色"),
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
    RoleRes(int status, String code, String msg) {
        this(status, false, code, msg);
    }

    /**
     * res
     *
     * @param thr  用力推
     * @param code 代码
     * @param msg  味精
     */
    RoleRes(boolean thr, String code, String msg) {
        this(200, thr, code, msg);
    }

    /**
     * res
     *
     * @param code 代码
     * @param msg  味精
     */
    RoleRes(String code, String msg) {
        this(200, false, code, msg);
    }

    @Override
    public boolean isSuccess() {
        return !thr;
    }
}