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
 * UI 卡片操作按钮。
 *
 * <p>定义 UI 卡片底部的一个操作按钮，用户点击后根据 {@code type} 执行对应操作。
 * 按钮的交互行为由 {@link ActionType} 决定：</p>
 * <ul>
 *   <li>{@link ActionType#API API} — 直调后端 API，不绕 AI</li>
 *   <li>{@link ActionType#LINK LINK} — 跳转链接</li>
 *   <li>{@link ActionType#SUBMIT SUBMIT} — 表单提交</li>
 *   <li>{@link ActionType#CALLBACK CALLBACK} — 结果回 AI 处理</li>
 * </ul>
 *
 * <pre>{@code
 * Action action = new Action();
 * action.setLabel("确认支付");
 * action.setType(ActionType.API);
 * action.setMethod("POST");
 * action.setUrl("/api/direct/orders/pay");
 * action.setParams(ParamMap.of("orderId", "ORD12345"));
 * }</pre>
 *
 * @see ActionType
 * @see ParamMap
 */
@Data
public class Action {
    /** 按钮文本 */
    private String label;
    /** 按钮类型，决定点击后的行为 */
    private ActionType type;
    /** HTTP 方法（仅 {@link ActionType#API} 时有效） */
    private String method;
    /** 目标 URL（{@link ActionType#API} 时为后端接口地址） */
    private String url;
    /** 请求参数 */
    private ParamMap params;
}
