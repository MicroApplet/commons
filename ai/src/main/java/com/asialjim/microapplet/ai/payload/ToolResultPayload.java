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

/**
 * 工具/函数调用结果负载。
 *
 * <p>工具执行完成后，将结果封装为此负载返回给 AI 或前端。
 * {@code tool} 字段关联对应的 {@link ToolCallPayload}。</p>
 *
 * @see ToolCallPayload
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolResultPayload {
    /** 工具/函数名称（与 ToolCallPayload.tool 对应） */
    private String tool;
    /** 执行结果（任意类型，通常为 POJO 或 Map） */
    private Object result;
}
