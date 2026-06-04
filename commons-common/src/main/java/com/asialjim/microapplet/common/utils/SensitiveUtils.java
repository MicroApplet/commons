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

package com.asialjim.microapplet.common.utils;

import com.asialjim.microapplet.common.human.IdCardType;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * 脱敏工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/15, &nbsp;&nbsp; <em>version:1.0</em>
 */
public abstract class SensitiveUtils {

    /**
     * 脱敏字符串
     *
     * @param source     {@link String 原字符串}
     * @param mask       {@link Character 掩码}
     * @param repeatMask {@link Integer 掩码字符数}
     * @param left       {@link Integer 保留左边字符数}
     * @param right      {@link Integer 保留右边字符数}
     * @since 2025/10/15
     */
    public static String sensitive(String source, char mask, int repeatMask, int left, int right) {
        return StringUtils.left(source, left) + StringUtils.repeat(mask, repeatMask) + StringUtils.right(source, right);
    }

    public static String sensitive(String source, int repeat, int left, int right) {
        return sensitive(source, '*', repeat, left, right);
    }

    /**
     * 证件脱敏
     *
     * @param type         {@link IdCardType type}
     * @param idCardNumber {@link String idCardNumber}
     * @return {@link String }
     * @since 2025/10/15
     */
    public static String idCard(IdCardType type, String idCardNumber) {
        if (StringUtils.isBlank(idCardNumber))
            return StringUtils.EMPTY;
        if (Objects.isNull(type))
            return StringUtils.EMPTY;
        int sensitiveLeft = type.getSensitiveLeft();
        int sensitiveRight = type.getSensitiveRight();
        return sensitive(idCardNumber, 4, sensitiveLeft, sensitiveRight);
    }

    /**
     * 中国手机号脱敏
     *
     * @param phone {@link String 手机号}
     * @since 2025/10/15
     */
    public static String chinaMobilePhone(String phone) {
        if (StringUtils.isBlank(phone) || StringUtils.length(phone)<11)
            return StringUtils.EMPTY;
        return sensitive(phone, 4, 3, 4);
    }

    /**
     * 中文姓名脱敏
     *
     * @param name {@link String name}
     * @return {@link String }
     * @since 2025/10/15
     */
    public static String chineseName(String name) {
        int length = StringUtils.length(name);
        if (length == 2)
            return sensitive(name, 1, 0, 1);

        if (length > 2)
            return sensitive(name, length - 2, 1, 1);
        return "*";
    }
}