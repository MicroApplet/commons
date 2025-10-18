/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.commons.security;

import java.util.Objects;

/**
 * 角色代码
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/15, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface RoleCode {
    String TOURIST_CODE = "tourist";
    long TOURIST_BIT = 1L;
    String TOURIST_DESC = "游客";

    String AUTHENTICATED_CODE = "authenticated";
    long AUTHENTICATED_BIT = TOURIST_BIT << 1;
    String AUTHENTICATED_DESC = "登录用户";

    String PHONE_CODE = "phone";
    long PHONE_BIT = TOURIST_BIT << 2;
    String PHONE_DESC = "手机号用户";

    String WECHAT_USER_CODE = "wechat";
    long WECHAT_USER_BIT = TOURIST_BIT << 3;
    String WECHAT_USER_DESC = "微信用户";

    String ID_CARD_USER_CODE = "id-card";
    long ID_CARD_USER_BIT = TOURIST_BIT << 4;
    String ID_CARD_USER_DESC = "实名证件用户";

    String BANK_CARD_USER_CODE = "bank-card";
    long BANK_CARD_USER_BIT = TOURIST_BIT << 7;
    String BANK_CARD_USER_DESC = "银行卡用户";


    // NOTICE:  50位以上角色位图分配给后台用户/员工用户
    String EMPLOYEE_CODE = "employee";
    long EMPLOYEE_BIT = TOURIST_BIT << 50;
    String EMPLOYEE_DESC = "员工";

    String CMS_CODE = "cms:user";
    long CMS_BIT = TOURIST_BIT << 51;
    String CMS_DESC = "后管用户";

    String SYSTEM_CODE = "system";
    long SYSTEM_BIT = TOURIST_BIT << 63;
    String SYSTEM_DESC = "系统管理员";

    // NOTICE: 登录的超管拥有所有权限，
    // NOTICE: 也就是说登录的超管用户的角色位图为 Long.MAX_VALUE, 超管用户登录时自动补充登录位
    // NOTICE: 但是未登录的超管用户，由于其未登录，导致登录用户角色的位缺失
    // NOTICE: 所以此处要去除掉登录位
    String ROOT_CODE = "root";
    long ROOT_BIT = Long.MAX_VALUE & ~AUTHENTICATED_BIT;
    String ROOT_DESC = "超级管理员";

    /**
     * 角色代码
     *
     * @since 2025/10/15
     */
    String getCode();

    /**
     * 角色位图
     *
     * @since 2025/10/15
     */
    long getBit();

    /**
     * 角色描述
     *
     * @since 2025/10/15
     */
    String getDesc();

    static boolean contains(RoleCode source, RoleCode target) {
        if (Objects.isNull(source) || Objects.isNull(target))
            return false;

        long sourceBit = source.getBit();
        long targetBit = target.getBit();
        return (sourceBit & targetBit) == targetBit;
    }

    static boolean contains(RoleCode source, Long target) {
        if (Objects.isNull(source) || Objects.isNull(target))
            return false;

        long sourceBit = source.getBit();
        long targetBit = target;
        return (sourceBit & targetBit) == targetBit;
    }


    static boolean contains(Long source, Long target) {
        if (Objects.isNull(source) || Objects.isNull(target))
            return false;

        long sourceBit = source;
        long targetBit = target;
        return (sourceBit & targetBit) == targetBit;
    }

    static boolean contains(Long source, RoleCode target) {
        if (Objects.isNull(source) || Objects.isNull(target))
            return false;

        long sourceBit = source;
        long targetBit = target.getBit();
        return (sourceBit & targetBit) == targetBit;
    }
}