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

package com.asialjim.microapplet.commons.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 用户角色
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/10, &nbsp;&nbsp; <em>version: 1.0</em>
 */
@Getter
@AllArgsConstructor
public enum UserRole implements Role {
    Tourist(0, "游客", "未登录用户"),
    Login(1, "登录用户", "已登录"),
    Phone(1 << 1, "手机号用户", "已授权手机号"),
    Identification(1 << 2, "证件号用户", "已绑定证件号"),

    Illegal(1L << 63, "非法角色", "非法角色");

    public RoleType getRoleType() {
        return RoleType.User;
    }

    private final long code;
    private final String name;
    private final String desc;
}