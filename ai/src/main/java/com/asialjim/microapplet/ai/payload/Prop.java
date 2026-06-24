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

package com.asialjim.microapplet.ai.payload;

import lombok.Data;

/**
 * UI 卡片属性定义。
 *
 * <p>描述 UI 卡片中的一条数据展示项。
 * {@code value} 为原始值（可能包含加密数据，用于前端回传后端解密），
 * {@code display} 为前端展示用的脱敏文本。</p>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 后端构建
 * Prop p = new Prop();
 * p.setKey("phone");
 * p.setLabel("手机号");
 * p.setValue("_mask|GM|a1b2|x3y4|z5w6|138****1234"); // 加密数据，用于回传
 * p.setDisplay("138****1234");                          // 脱敏文本，用于渲染
 *
 * // 前端渲染：label + display → "手机号: 138****1234"
 * // 前端回传：value → "_mask|GM|a1b2|x3y4|z5w6|138****1234"
 * }</pre>
 */
@Data
public class Prop {
    /** 属性键（程序化标识） */
    private String key;
    /** 属性标签（展示给用户看的名称） */
    private String label;
    /** 属性值（原始数据，可能含加密格式 {@code _mask|...}，用于回传后端解密） */
    private Object value;
    /** 展示值（脱敏后的文本，前端直接渲染此字段） */
    private String display;
}
