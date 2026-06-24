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

import com.asialjim.microapplet.ai.payload.*;
import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * SSE 事件。
 *
 * <p>AI 与前端通信的事件载体，包含事件类型({@link SseEventType})和负载数据({@code payload})。
 * 服务端通过 SSE 协议将事件推送至前端，前端根据事件类型做差异化展示。</p>
 *
 * <h3>SSE 协议帧格式</h3>
 * <pre>{@code
 * event: text
 * data: {"text":"你好","tts":"你好"}
 *
 * event: ui
 * data: {"uiType":"card","title":"订单","props":[...],"actions":[...]}
 *
 * event: done
 * data: [DONE]
 * }</pre>
 *
 * <h3>使用示例</h3>
 * <pre>{@code
 * // 发送文本回复
 * SseEvent.text("你好").toSseFrame()
 *
 * // 发送 UI 卡片
 * UiPayload card = new UiPayload();
 * card.setUiType(UiType.CARD);
 * card.setTitle("订单详情");
 * SseEvent.ui(card).toSseFrame()
 *
 * // 发送工具调用
 * SseEvent.toolCall("queryOrder", Map.of("orderId","123")).toSseFrame()
 * }</pre>
 *
 * @param <T> 负载数据类型
 * @see SseEventType
 * @see UiPayload
 */
@Data
public class SseEvent<T> {

    /** 事件类型 */
    private SseEventType type;
    /** 事件负载数据 */
    private T payload;

    public SseEvent() {}

    public SseEvent(SseEventType type, T payload) {
        this.type = type;
        this.payload = payload;
    }

    /**
     * 获取 SSE 事件名称。
     *
     * @return 事件名称，如 "text"、"ui"、"done"
     */
    public String eventName() {
        return Optional.ofNullable(type).map(SseEventType::getEventName).orElse(StringUtils.EMPTY);
    }

    /**
     * 生成 SSE 协议的 {@code event:} 行。
     *
     * @return {@code event: <typeName>}
     */
    public String getEventLine() {
        return "event: " + type.getEventName();
    }

    /**
     * 生成 SSE 协议的 {@code data:} 行。
     * <ul>
     *   <li>如果负载为空，返回 {@code data: }</li>
     *   <li>如果事件类型为 {@link SseEventType#DONE DONE}，返回 {@code data: [DONE]}</li>
     *   <li>否则将负载序列化为 JSON，返回 {@code data: <json>}</li>
     * </ul>
     *
     * @return SSE {@code data:} 行
     */
    public String getDataLine() {
        if (payload == null) return "data: ";
        if (type == SseEventType.DONE) return "data: [DONE]";
        try {
            return "data: " + JsonUtil.instance.toStr(payload);
        } catch (Exception e) {
            return "data: {}";
        }
    }

    /**
     * 获取负载的 JSON 字符串表示。
     *
     * @return JSON 字符串
     */
    public String payloadStr() {
        if (payload == null) return "";
        if (type == SseEventType.DONE) return "[DONE]";
        try {
            return JsonUtil.instance.toStr(payload);
        } catch (Exception e) {
            return "{}";
        }
    }

    /**
     * 生成完整的 SSE 协议帧。
     *
     * @return {@code event: ...\ndata: ...\n\n} 格式的字符串，可直接写入 SSE 响应流
     */
    public String toSseFrame() {
        return getEventLine() + "\n" + getDataLine() + "\n\n";
    }

    // ==================== 工厂方法 ====================

    /**
     * 创建 AI 思考过程事件。
     *
     * @param text 思考内容
     * @return SSE 事件
     */
    public static SseEvent<ThinkingPayload> thinking(String text) {
        return new SseEvent<>(SseEventType.THINKING, new ThinkingPayload(text));
    }

    /**
     * 创建工具调用事件。
     *
     * @param tool   工具名称
     * @param params 调用参数
     * @return SSE 事件
     */
    public static SseEvent<ToolCallPayload> toolCall(String tool, Map<String, Object> params) {
        return new SseEvent<>(SseEventType.TOOL_CALL, new ToolCallPayload(tool, params));
    }

    /**
     * 创建工具执行结果事件。
     *
     * @param tool   工具名称
     * @param result 执行结果
     * @return SSE 事件
     */
    public static SseEvent<ToolResultPayload> toolResult(String tool, Object result) {
        return new SseEvent<>(SseEventType.TOOL_RESULT, new ToolResultPayload(tool, result));
    }

    /**
     * 创建文本回复事件。
     *
     * @param text 回复文本
     * @return SSE 事件
     */
    public static SseEvent<TextPayload> text(String text) {
        return new SseEvent<>(SseEventType.TEXT, new TextPayload(text));
    }

    /**
     * 创建文本回复事件（含语音合成文本）。
     *
     * @param text 回复文本
     * @param tts  语音合成文本
     * @return SSE 事件
     */
    public static SseEvent<TextPayload> text(String text, String tts) {
        return new SseEvent<>(SseEventType.TEXT, new TextPayload(text, tts));
    }

    /**
     * 创建结构化数据事件。
     *
     * @param schema 数据结构描述
     * @param data   数据列表
     * @return SSE 事件
     */
    public static SseEvent<DataPayload<Map<String, Object>>> data(Object schema, List<Map<String, Object>> data) {
        return new SseEvent<>(SseEventType.DATA, new DataPayload<>(schema, data));
    }

    /**
     * 创建结构化数据事件（含摘要）。
     *
     * @param schema  数据结构描述
     * @param data    数据列表
     * @param summary 摘要文本
     * @return SSE 事件
     */
    public static SseEvent<DataPayload<Map<String, Object>>> data(Object schema, List<Map<String, Object>> data, String summary) {
        return new SseEvent<>(SseEventType.DATA, new DataPayload<>(schema, data, summary));
    }

    /**
     * 创建动态 UI 卡片事件。
     *
     * @param ui UI 卡片负载
     * @return SSE 事件
     * @see UiPayload
     */
    public static SseEvent<UiPayload> ui(UiPayload ui) {
        return new SseEvent<>(SseEventType.UI, ui);
    }

    /**
     * 创建错误事件。
     *
     * @param code    错误码
     * @param message 错误描述
     * @return SSE 事件
     */
    public static SseEvent<ErrorPayload> error(String code, String message) {
        return new SseEvent<>(SseEventType.ERROR, new ErrorPayload(code, message));
    }

    /**
     * 创建需要用户补充输入事件。
     *
     * @param question 提问给用户的问题
     * @param hint     输入提示
     * @return SSE 事件
     */
    public static SseEvent<RequireInputPayload> requireInput(String question, String hint) {
        return new SseEvent<>(SseEventType.REQUIRE_INPUT, new RequireInputPayload(question, hint));
    }

    /**
     * 创建会话结束事件。
     * <p>前端收到此事件后应关闭加载状态，表示 AI 响应完成。</p>
     *
     * @return SSE 事件
     */
    public static SseEvent<String> done() {
        return new SseEvent<>(SseEventType.DONE, "[DONE]");
    }
}
