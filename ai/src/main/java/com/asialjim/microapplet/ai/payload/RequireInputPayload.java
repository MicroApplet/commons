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
 * 需要用户补充输入负载。
 *
 * <p>当 AI 处理用户请求时发现信息不足以完成操作，需要用户补充输入。
 * 前端根据此负载展示一个输入框或选择界面，用户提交后再将结果发送给 AI 继续处理。</p>
 *
 * <pre>{@code
 * // AI 需要用户选择订单
 * SseEvent.requireInput("请选择要查询的订单", "输入订单号或选择下方列表")
 *
 * // AI 需要用户确认信息
 * SseEvent.requireInput("请确认您的手机号", "输入手机号")
 * }</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequireInputPayload {
    /** 向用户提出的问题 */
    private String question;
    /** 输入提示文本 */
    private String hint;
    /** 期望的输入类型：{@code text} / {@code number} / {@code date} / {@code choice} / {@code file} 等 */
    private String expectedType;

    /**
     * 构造需要用户补充输入负载，默认期望文本输入。
     *
     * @param question 向用户提出的问题
     * @param hint     输入提示
     */
    public RequireInputPayload(String question, String hint) {
        this.question = question;
        this.hint = hint;
        this.expectedType = "text";
    }
}
