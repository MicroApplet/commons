package com.asialjim.microapplet.sensitive.encrypt;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AlgorithmModeTest {

    @Test
    void testEnumValues() {
        assertEquals(2, AlgorithmMode.values().length);
    }

    @Test
    void testGM() {
        AlgorithmMode gm = AlgorithmMode.GM;
        assertEquals("GM", gm.getCode());
        assertEquals("SM4", gm.getEncAlgorithm());
        assertEquals("HmacSM3", gm.getMacAlgorithm());
    }

    @Test
    void testModern() {
        AlgorithmMode modern = AlgorithmMode.MODERN;
        assertEquals("MODERN", modern.getCode());
        assertEquals("ChaCha20", modern.getEncAlgorithm());
        assertEquals("ChaCha20", modern.getMacAlgorithm());
    }

    @Test
    void testFromCode() {
        assertEquals(AlgorithmMode.GM, AlgorithmMode.fromCode("GM"));
        assertEquals(AlgorithmMode.MODERN, AlgorithmMode.fromCode("MODERN"));
    }

    @Test
    void testFromCodeInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                AlgorithmMode.fromCode("UNKNOWN"));
    }

    @Test
    void testFromCodeCaseSensitive() {
        assertThrows(IllegalArgumentException.class, () ->
                AlgorithmMode.fromCode("gm"));
    }
}
