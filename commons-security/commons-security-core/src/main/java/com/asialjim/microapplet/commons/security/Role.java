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
public enum Role implements RoleCode {
    /**
     * 登录用户
     */
    Authenticated(AUTHENTICATED_CODE, AUTHENTICATED_BIT, AUTHENTICATED_DESC),
    /**
     * 手机用户
     */
    Phone(PHONE_CODE, PHONE_BIT, PHONE_DESC),
    /**
     * 微信用户
     */
    WeChatUser(WECHAT_USER_CODE, WECHAT_USER_BIT, WECHAT_USER_DESC),
    /**
     * 证件用户
     */
    IdCardUser(ID_CARD_USER_CODE, ID_CARD_USER_BIT, ID_CARD_USER_DESC),
    /**
     * 银行卡用户
     */
    BankCardUser(BANK_CARD_USER_CODE, BANK_CARD_USER_BIT, BANK_CARD_USER_DESC),

    /**
     * 员工
     */
    Employee(EMPLOYEE_CODE, EMPLOYEE_BIT, EMPLOYEE_DESC),
    /**
     * 后管用户
     */
    CMSUser(CMS_CODE, CMS_BIT, CMS_DESC),
    /**
     * 系统管理员
     */
    System(SYSTEM_CODE, SYSTEM_BIT, SYSTEM_DESC),
    /**
     * 超管
     */
    Root(ROOT_CODE, ROOT_BIT, ROOT_DESC),
    /**
     * 游客
     */
    Tourist(TOURIST_CODE, TOURIST_BIT, TOURIST_DESC);

    public final String code;
    private final long bit;
    private final String desc;
}