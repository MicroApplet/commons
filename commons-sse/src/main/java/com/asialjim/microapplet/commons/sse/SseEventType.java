package com.asialjim.microapplet.commons.sse;

/**
 * SSE 事件类型枚举
 * 对应 SSE 协议中的 event: 字段值
 */
public enum SseEventType {

    /** AI 思考过程反馈 */
    THINKING("thinking"),
    /** 正在调用工具 */
    TOOL_CALL("tool_call"),
    /** 工具执行结果 */
    TOOL_RESULT("tool_result"),
    /** 纯文本回复 */
    TEXT("text"),
    /** 结构化数据 */
    DATA("data"),
    /** 动态 UI 卡片 */
    UI("ui"),
    /** 错误信息 */
    ERROR("error"),
    /** 需要用户补充输入 */
    REQUIRE_INPUT("require_input"),
    /** 会话结束标识 */
    DONE("done");

    private final String eventName;

    SseEventType(String eventName) {
        this.eventName = eventName;
    }

    /** 获取 SSE 协议中的 event 名称 */
    public String getEventName() {
        return eventName;
    }

    public static SseEventType fromEventName(String name) {
        for (SseEventType t : values()) {
            if (t.eventName.equals(name)) return t;
        }
        return TEXT;
    }
}
