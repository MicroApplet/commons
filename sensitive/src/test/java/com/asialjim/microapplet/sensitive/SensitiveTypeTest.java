package com.asialjim.microapplet.sensitive;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SensitiveTypeTest {

    @Test
    void testEnumValues() {
        assertEquals(8, SensitiveType.values().length);
    }

    @Test
    void testBankCard() {
        assertEquals(SensitiveType.BankCard, SensitiveType.BankCard);
        assertEquals("^[1-9]\\d{12,18}$", SensitiveType.BankCard.getRegex());
        assertEquals(6, SensitiveType.BankCard.getPrefix());
        assertEquals(4, SensitiveType.BankCard.getSuffix());
    }

    @Test
    void testChineseMobilePhone() {
        assertEquals("^1[3-9]\\d{9}$", SensitiveType.ChineseMobilePhone.getRegex());
        assertEquals(3, SensitiveType.ChineseMobilePhone.getPrefix());
        assertEquals(4, SensitiveType.ChineseMobilePhone.getSuffix());
    }

    @Test
    void testChineseCitizenIdCard() {
        assertTrue(SensitiveType.ChineseCitizenIdCard.getPrefix() > 0);
        assertTrue(SensitiveType.ChineseCitizenIdCard.getSuffix() > 0);
    }

    @Test
    void testCustomerType() {
        assertEquals("^[\\p{L}\\p{N}]*$", SensitiveType.Customer.getRegex());
        assertEquals(1, SensitiveType.Customer.getPrefix());
        assertEquals(1, SensitiveType.Customer.getSuffix());
    }
}
