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

package com.asialjim.microapplet.ai.event;

import lombok.Getter;

/**
 * SSE 事件类型枚举。
 *
 * <p>定义 AI 与前端通信的应用层协议事件类型，对应 SSE 协议中 {@code event:} 字段的值。
 * 每种事件类型代表 AI 输出的一种内容形态，前端根据事件类型做不同的展示处理。</p>
 *
 * <h3>事件类型一览</h3>
 * <pre>{@code
 * thinking      → AI 思考过程（流式显示思维链）
 * tool_call     → AI 调用了某个工具/函数
 * tool_result   → 工具执行结果返回
 * text          → 纯文本回复（可含语音合成文本）
 * data          → 结构化数据（表格、列表等）
 * ui            → 动态 UI 卡片（表单、确认框等）
 * error         → 错误信息
 * require_input → AI 需要用户补充输入
 * done          → 会话结束标识
 * }</pre>
 *
 * @see SseEvent
 * @see com.asialjim.microapplet.ai.payload.UiPayload
 * @see com.asialjim.microapplet.ai.payload.TextPayload
 */
@Getter
public enum SseEventType {

    /** AI 思考过程反馈，用于在界面上展示 AI 的推理步骤 */
    THINKING("thinking"),
    /** AI 正在调用外部工具/函数，参数跟随事件下发 */
    TOOL_CALL("tool_call"),
    /** 工具执行结果返回给前端 */
    TOOL_RESULT("tool_result"),
    /** 纯文本回复，可附带语音合成文本 */
    TEXT("text"),
    /** 结构化数据，如表格数据、列表数据等 */
    DATA("data"),
    /** 动态 UI 卡片，前端根据 {@link com.asialjim.microapplet.ai.payload.UiPayload} 渲染交互界面 */
    UI("ui"),
    /** 错误信息，携带错误码和错误描述 */
    ERROR("error"),
    /** AI 需要用户补充输入信息才能继续 */
    REQUIRE_INPUT("require_input"),
    /** AI 响应结束标识，前端收到此事件后关闭加载状态 */
    DONE("done");

    /**
     * -- GETTER --
     *  获取 SSE 协议中的事件名称（小写），用于
     *  字段。
     *
     * @return 事件名称字符串
     */
    private final String eventName;

    SseEventType(String eventName) {
        this.eventName = eventName;
    }

    /**
     * 根据事件名称字符串解析枚举值。
     *
     * @param name 事件名称（大小写敏感）
     * @return 对应的枚举值，未知名称时返回 {@link #TEXT}
     */
    public static SseEventType fromEventName(String name) {
        for (SseEventType t : values()) {
            if (t.eventName.equals(name)) return t;
        }
        return TEXT;
    }
}
