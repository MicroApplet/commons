package com.asialjim.microapplet.sensitive.log;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensitiveLogTest {

    @Test
    void testFmtWithPrimitiveArgs() {
        String result = SensitiveLog.fmt("user: {}", "test");
        assertEquals("user: test", result);
    }

    @Test
    void testFmtWithNoArgs() {
        String result = SensitiveLog.fmt("just a message");
        assertEquals("just a message", result);
    }

    @Test
    void testFmtWithNullArgs() {
        String result = SensitiveLog.fmt("msg {}", new Object[]{null});
        assertEquals("msg null", result);
    }

    @Test
    void testFmtWithEmptyMessage() {
        String result = SensitiveLog.fmt("");
        assertEquals("", result);
    }

    @Test
    void testFmtWithMultipleArgs() {
        String result = SensitiveLog.fmt("a={}, b={}", "1", "2");
        assertEquals("a=1, b=2", result);
    }

    @SuppressWarnings("DataFlowIssue")
    @Test
    void testFmtWithNullMessage() {
        assertNull(SensitiveLog.fmt(null));
    }

    @Test
    void testFmtWithObjectArg() {
        String result = SensitiveLog.fmt("obj: {}", new ObjectArg("hello"));
        assertTrue(result.contains("\"name\""));
    }

    record ObjectArg(String name) {}
}
