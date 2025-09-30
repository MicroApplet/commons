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
 * 角色
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum Role {
    Authenticated("authenticated", 1 << 1, "登录用户"),
    Phone("phone", 1 << 2, "手机号用户"),
    WeChatUser("wechat", 1 << 3, "微信用户"),
    WeChatMp("wechat:mp", (1 << 3) + (1 << 4), "微信公众号用户"),
    WeChatApplet("wechat:applet", (1 << 3) + 1 << 5, "微信小程序用户"),
    IdCardUser("id-card", 1 << 6, "实名证件用户"),
    BankCardUser("bank-card", 1 << 7, "银行卡用户"),


    Employee("employee", 1L << 61, "员工"),
    CMSUser("cms:user", 1L << 62, "后管用户"),
    System("system", 1L << 63, "系统管理员"),
    Root("root", Long.MAX_VALUE, "超级管理员"),
    Tourist("tourist", 1, "游客");

    public final String code;
    private final long bit;
    private final String desc;

    public boolean is(long role) {
        return (this.bit & role) == this.bit;
    }
}