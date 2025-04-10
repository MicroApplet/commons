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

package com.asialjim.microapplet.common.human;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 性别
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum Gender {
    None("", "未提供"),
    Male("0", "女"),
    Female("1", "男");

    private final String code;
    private final String desc;

    public static Gender codeOf(String code) {
        if (StringUtils.isBlank(code))
            return None;
        switch (code) {
            case "0":
                return Male;
            case "1":
                return Female;
            default:
                return None;
        }
    }

    public static Gender descOf(String desc) {
        if (StringUtils.isBlank(desc))
            return None;
        switch (desc) {
            case "女":
                return Male;
            case "男":
                return Female;
            default:
                return None;
        }
    }
}