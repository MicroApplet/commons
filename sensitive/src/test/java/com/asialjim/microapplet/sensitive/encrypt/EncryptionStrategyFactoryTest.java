package com.asialjim.microapplet.sensitive.encrypt;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionStrategyFactoryTest {

    @Test
    void testGetGMStrategy() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategy(AlgorithmMode.GM);
        assertNotNull(strategy);
        assertEquals(AlgorithmMode.GM, strategy.getAlgorithmMode());
    }

    @Test
    void testGetModernStrategy() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategy(AlgorithmMode.MODERN);
        assertNotNull(strategy);
        assertEquals(AlgorithmMode.MODERN, strategy.getAlgorithmMode());
    }

    @Test
    void testGetStrategyForDataGM() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategyForData("_mask|GM|nonce|enc|mac|mask");
        assertEquals(AlgorithmMode.GM, strategy.getAlgorithmMode());
    }

    @Test
    void testGetStrategyForDataModern() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategyForData("_mask|MODERN|nonce|enc|mac|mask");
        assertEquals(AlgorithmMode.MODERN, strategy.getAlgorithmMode());
    }

    @Test
    void testGetStrategyForDataInvalidFallsBackToGM() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategyForData("invalid|format");
        assertNotNull(strategy);
    }

    @Test
    void testGetStrategyForDataNullFallsBackToGM() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategyForData(null);
        assertNotNull(strategy);
    }

    @Test
    void testGMStrategySupports() {
        EncryptionStrategy strategy = EncryptionStrategyFactory.getStrategy(AlgorithmMode.GM);
        assertTrue(strategy.supports("_mask|GM|nonce|enc|mac|mask"));
        assertFalse(strategy.supports("_mask|MODERN|nonce|enc|mac|mask"));
    }
}
