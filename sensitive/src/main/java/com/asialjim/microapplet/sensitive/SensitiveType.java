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

package com.asialjim.microapplet.sensitive;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 敏感数据类型
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Getter
@AllArgsConstructor
public enum SensitiveType {

    BankCard("^[1-9]\\d{12,18}$", 6, 4),
    EMail("^(?i)[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?$", 3, 4),
    ChineseCitizenIdCard("(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{2}[0-9Xx]$)|(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)", 6, 4),
    ChineseMobilePhone("^1[3-9]\\d{9}$", 3, 4),
    ChineseTellPhone("^(0\\d{2,3}[-\\s]?)?\\d{7,8}([-\\s]?\\d{1,6})?$", 3, 4),
    ChineseName("^[\\u4e00-\\u9fa5]+(·[\\u4e00-\\u9fa5]+)*$", 1, 1),
    EnglishName("^[A-Za-z][A-Za-z'\\-.]{1,19}(?:\\s+[A-Za-z][A-Za-z'\\-.]{1,19})*$", 1, 1),
    Customer("^[\\p{L}\\p{N}]*$", 1, 1);

    private final String regex;
    private final int prefix;
    private final int suffix;
}
