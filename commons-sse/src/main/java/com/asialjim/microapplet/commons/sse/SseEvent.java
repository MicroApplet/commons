package com.asialjim.microapplet.commons.sse;

import com.asialjim.microapplet.commons.sse.payload.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Map;

public class SseEvent<T> {
    private static final ObjectMapper mapper = new ObjectMapper();
    private SseEventType type;
    private T payload;

    public SseEvent() {}
    public SseEvent(SseEventType type, T payload) { this.type = type; this.payload = payload; }

    public SseEventType getType() { return type; }
    public void setType(SseEventType type) { this.type = type; }
    public T getPayload() { return payload; }
    public void setPayload(T payload) { this.payload = payload; }

    public String getEventLine() { return "event: " + type.getEventName(); }
    public String getDataLine() {
        if (payload == null) return "data: ";
        if (type == SseEventType.DONE) return "data: [DONE]";
        try { return "data: " + mapper.writeValueAsString(payload); } catch (Exception e) { return "data: {}"; }
    }
    public String toSseFrame() { return getEventLine() + "\n" + getDataLine() + "\n\n"; }

    public static SseEvent<ThinkingPayload> thinking(String text) { return new SseEvent<>(SseEventType.THINKING, new ThinkingPayload(text)); }
    public static SseEvent<ToolCallPayload> toolCall(String tool, Map<String, Object> params) { return new SseEvent<>(SseEventType.TOOL_CALL, new ToolCallPayload(tool, params)); }
    public static SseEvent<ToolResultPayload> toolResult(String tool, Object result) { return new SseEvent<>(SseEventType.TOOL_RESULT, new ToolResultPayload(tool, result)); }
    public static SseEvent<TextPayload> text(String text) { return new SseEvent<>(SseEventType.TEXT, new TextPayload(text)); }
    public static SseEvent<TextPayload> text(String t, String tts) { return new SseEvent<>(SseEventType.TEXT, new TextPayload(t, tts)); }
    public static SseEvent<DataPayload<Map<String, Object>>> data(Object s, List<Map<String, Object>> d) { return new SseEvent<>(SseEventType.DATA, new DataPayload<>(s, d)); }
    public static SseEvent<DataPayload<Map<String, Object>>> data(Object s, List<Map<String, Object>> d, String sum) { return new SseEvent<>(SseEventType.DATA, new DataPayload<>(s, d, sum)); }
    public static SseEvent<UiPayload> ui(UiPayload p) { return new SseEvent<>(SseEventType.UI, p); }
    public static SseEvent<ErrorPayload> error(String c, String m) { return new SseEvent<>(SseEventType.ERROR, new ErrorPayload(c, m)); }
    public static SseEvent<RequireInputPayload<Object>> requireInput(String q, String h) { return new SseEvent<>(SseEventType.REQUIRE_INPUT, new RequireInputPayload<>(q, h)); }
    public static SseEvent<String> done() { return new SseEvent<>(SseEventType.DONE, "[DONE]"); }
}
