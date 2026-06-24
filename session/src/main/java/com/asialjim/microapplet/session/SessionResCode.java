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

package com.asialjim.microapplet.session;

import com.asialjim.microapplet.commons.standard.context.ResCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 会话错误代码。
 * 代码范围： 800000 - 810000
 */
@Getter
@AllArgsConstructor
public enum SessionResCode implements ResCode {
    LoginFailure(401,false,"800001","登录失败"),
    Empty(401,false,"800001","找不到会话信息"),
    Forbid4Tourist(403,false,"800002","游客禁止使用该功能"),

    EmptyPlatformType(200,false,"800201","找不到会话所属平台类型"),
    EmptyPlatformAppid(200,false,"800202","找不到会话所属平台应用编号"),
    EmptyPlatformAppType(200,false,"800203","找不到会话所属平台应用类型"),
    UnSupportPlatformType(200,false,"800204","不支持的平台类型"),
    UnSupportPlatformAppType(200,false,"800205","不支持的平台应用类型"),

    EmptyOpenId(200,false,"800301","找不到会话渠道用户OpenId"),
    EmptyUnionId(200,false,"800302","找不到会话渠道用户UnionId"),
    EmptySessionKey(200,false,"800303","找不到会话渠道用户会话令牌"),

    EmptyAppTypeUserEncrypter(200,false,"800401","找不到该应用类型的用户加密通讯处理器"),

    TokenHMacException(401,false,"800501","会话令牌签名失败"),
    TokenKeyMissException(401,false,"800502","会话令牌秘钥缺失"),
    TokenKeyUnConfigException(401,false,"800503","会话令牌秘钥未配置"),
    TokenKeyTooWeekException(401,false,"800504","会话令牌秘钥长度不足，请提供超过16位的秘钥"),

    UserIdCardTypeEmptyErr(200,false,"800601","用户证件类型不能为空"),

    EmptyPlatformAppTypeEncService(200,false,"800210","找不到会话所属平台应用秘钥服务器"),

    OK(200, true, "0", "OK");

    private final int status;
    private final boolean success;
    private final String code;
    private final String msg;

    @Override
    public boolean isThr() {
        return !success;
    }
}
