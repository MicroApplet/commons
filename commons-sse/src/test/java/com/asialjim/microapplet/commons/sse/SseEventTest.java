package com.asialjim.microapplet.commons.sse;

import com.asialjim.microapplet.commons.sse.payload.*;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class SseEventTest {

    @Test void thinking() {
        var e = SseEvent.thinking("思考中");
        assertEquals(SseEventType.THINKING, e.getType());
        assertEquals("思考中", ((ThinkingPayload)e.getPayload()).getText());
    }

    @Test void text() {
        var e = SseEvent.text("您好");
        assertEquals("event: text", e.getEventLine());
        assertEquals("data: {\"text\":\"您好\",\"tts\":\"您好\"}", e.getDataLine());
    }

    @Test void ui() {
        var e = SseEvent.ui(new UiPayload());
        assertEquals(SseEventType.UI, e.getType());
    }

    @Test void error() {
        var e = SseEvent.error("ERR001", "出错了");
        assertEquals("event: error", e.getEventLine());
        assertTrue(e.getDataLine().contains("ERR001"));
    }

    @Test void done() {
        var e = SseEvent.done();
        assertEquals("event: done", e.getEventLine());
        assertEquals("data: [DONE]", e.getDataLine());
    }

    @Test void toolCall() {
        var e = SseEvent.toolCall("query", Map.of("userId","123"));
        assertEquals("event: tool_call", e.getEventLine());
        assertTrue(e.getDataLine().contains("query"));
    }

    @Test void toolResult() {
        var e = SseEvent.toolResult("query", Map.of("status","ok"));
        assertEquals(SseEventType.TOOL_RESULT, e.getType());
    }

    @Test void requireInput() {
        var e = SseEvent.requireInput("请输入姓名","姓名或ID");
        assertEquals("event: require_input", e.getEventLine());
    }

    @Test void dataEvent() {
        var e = SseEvent.data(Map.of("type","object"), List.of(Map.of("name","张三")));
        assertEquals("event: data", e.getEventLine());
    }

    @Test void toSseFrame() {
        var e = SseEvent.text("您好");
        String frame = e.toSseFrame();
        assertTrue(frame.startsWith("event: text"));
        assertTrue(frame.contains("data: "));
        assertTrue(frame.endsWith("\n\n"));
    }

    @Test void eventName() {
        assertEquals("thinking", SseEvent.thinking("").eventName());
        assertEquals("text", SseEvent.text("").eventName());
        assertEquals("done", SseEvent.done().eventName());
    }

    @Test void payloadStr() {
        assertEquals("[DONE]", SseEvent.done().payloadStr());
        assertTrue(SseEvent.text("hi").payloadStr().contains("hi"));
    }
}
