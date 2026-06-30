package com.asialjim.microapplet.sensitive.encrypt;

import org.junit.jupiter.api.Test;

import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;

class EncryptionResultTest {

    @Test
    void testCreateAndFormat() {
        AlgorithmMode mode = AlgorithmMode.GM;
        byte[] nonce = "1234567890123456".getBytes();
        byte[] encrypt = "encryptedData".getBytes();
        byte[] mac = "macData".getBytes();

        EncryptionResult result = new EncryptionResult(mode, nonce, encrypt, mac);

        assertEquals(mode, result.algorithmMode());
        assertArrayEquals(nonce, result.nonce());
        assertArrayEquals(encrypt, result.encrypt());
        assertArrayEquals(mac, result.mac());
    }

    @Test
    void testToFormattedString() {
        AlgorithmMode mode = AlgorithmMode.GM;
        byte[] nonce = "1234567890123456".getBytes();
        byte[] encrypt = "enc".getBytes();
        byte[] mac = "mac".getBytes();

        EncryptionResult result = new EncryptionResult(mode, nonce, encrypt, mac);
        String formatted = result.toFormattedString();

        assertTrue(formatted.startsWith("_mask"));
        assertTrue(formatted.contains("|GM|"));
    }

    @Test
    void testWithMask() {
        AlgorithmMode mode = AlgorithmMode.GM;
        EncryptionResult result = new EncryptionResult(mode, "nonce1234".getBytes(), "enc".getBytes(), "mac".getBytes());
        String withMask = result.withMask("138****1234");

        assertTrue(withMask.endsWith("138****1234"));
        assertTrue(withMask.startsWith("_mask|GM|"));
    }

    @Test
    void testIsEncryptionMaskData() {
        assertTrue(EncryptionResult.isEncryptionMaskData("_mask|GM|nonce|enc|mac|138****1234"));
        assertFalse(EncryptionResult.isEncryptionMaskData("plaintext"));
        assertFalse(EncryptionResult.isEncryptionMaskData("_mask|GM|nonce|enc|mac")); // only 5 parts
        assertFalse(EncryptionResult.isEncryptionMaskData(""));
        assertFalse(EncryptionResult.isEncryptionMaskData(null));
    }

    @Test
    void testFromFormattedString() {
        String formatted = "_mask|GM|bm9uY2U=|ZW5j|bWFj|138****1234";
        EncryptionResult result = EncryptionResult.fromFormattedString(formatted);

        assertEquals(AlgorithmMode.GM, result.algorithmMode());
        assertArrayEquals(Base64.getUrlDecoder().decode("bm9uY2U="), result.nonce());
        assertArrayEquals(Base64.getUrlDecoder().decode("ZW5j"), result.encrypt());
        assertArrayEquals(Base64.getUrlDecoder().decode("bWFj"), result.mac());
    }

    @Test
    void testFromFormattedStringInvalidThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                EncryptionResult.fromFormattedString("not_mask|GM|a|b|c|d"));
        assertThrows(IllegalArgumentException.class, () ->
                EncryptionResult.fromFormattedString("_mask|INVALID|a|b|c|d"));
    }
}
