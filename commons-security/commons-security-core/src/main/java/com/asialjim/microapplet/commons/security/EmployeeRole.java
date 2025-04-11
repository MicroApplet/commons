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

@Getter
@AllArgsConstructor
public enum EmployeeRole implements Role {
    Tourist(0, "游客", "未登录用户"),
    Login(1, "登录用户", "已登录用户"),
    Employee(1 << 1, "雇员", "企业雇员"),
    Manager((1 << 2) + (1 << 1), "员工", "企业员工"),

    System(1L << 61, "系统管理员", "系统管理员"),
    Root(Long.MAX_VALUE, "超级管理员", "超级管理员"),
    Illegal(1L << 63, "非法用户", "非法用户");

    public RoleType getRoleType() {
        return RoleType.Employee;
    }

    private final long code;
    private final String name;
    private final String desc;
}
