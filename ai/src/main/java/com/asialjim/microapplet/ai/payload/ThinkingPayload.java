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
 * AI 思考过程负载。
 *
 * <p>当 AI 在生成最终回复之前进行推理、分析或搜索时，
 * 通过此负载将中间思考过程推送给前端，实现思维链(Chain-of-Thought)展示效果。</p>
 *
 * <pre>{@code
 * SseEvent.thinking("正在查询用户信息...")
 * }</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThinkingPayload {
    /** 思考过程文本 */
    private String text;
}
