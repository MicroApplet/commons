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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * 工具/函数调用负载。
 *
 * <p>AI 判断需要调用外部工具或函数时，通过此负载下发工具名称和参数。
 * 前端或服务端工具执行层根据 {@code tool} 名称调用对应的处理逻辑，
 * 执行结果通过 {@link ToolResultPayload} 返回。</p>
 *
 * @see ToolResultPayload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolCallPayload {
    /** 工具/函数名称 */
    private String tool;
    /** 调用参数表 */
    private Map<String, Object> params;
}
