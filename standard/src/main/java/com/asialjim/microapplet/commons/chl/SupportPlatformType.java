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

package com.asialjim.microapplet.commons.chl;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.Strings;

@Getter
@AllArgsConstructor
public enum SupportPlatformType implements PlatformType {
    WeChat("wx","wechat","微信"),
    DouYin("dy","douyin","抖音")
    ;
    private final String shortCode;
    private final String code;
    private final String name;

    public static PlatformType hostOf(String host) {
        for (SupportPlatformType value : values()) {
            if (Strings.CI.containsAny(host,value.getCode(),value.getShortCode())) {
                return value;
            }
        }
        return null;
    }
}
