package com.asialjim.microapplet.sensitive.handler;

import com.asialjim.microapplet.sensitive.SensitiveType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SensitiveHandlerTest {

    @BeforeEach
    void setUp() {
        new ChineseMobilePhoneSensitiveHandler();
        new ChineseCitizenIdCardSensitiveHandler();
        new BankCardSensitiveHandler();
        new EMailSensitiveHandler();
        new ChineseNameSensitiveHandler();
        new EnglishNameSensitiveHandler();
        new ChineseTellPhoneSensitiveHandler();
        new CustomerSensitiveHandler();
    }

    @Test
    void testMaskMobilePhone() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseMobilePhone, "13812345678");
        assertEquals("138****5678", result);
    }

    @Test
    void testMaskIdCard() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseCitizenIdCard, "110101199001011234");
        assertEquals("110101****1234", result);
    }

    @Test
    void testMaskBankCard() {
        String result = SensitiveHandler.mask(SensitiveType.BankCard, "6222021234561234");
        assertEquals("622202******1234", result);
    }

    @Test
    void testMaskEmail() {
        String result = SensitiveHandler.mask(SensitiveType.EMail, "test@example.com");
        assertEquals("tes****xample.com", result);
    }

    @Test
    void testMaskChineseName() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseName, "张三");
        assertEquals("张*", result);
    }

    @Test
    void testMaskThreeCharChineseName() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseName, "李慕白");
        assertEquals("李*白", result);
    }

    @Test
    void testMaskEnglishName() {
        String result = SensitiveHandler.mask(SensitiveType.EnglishName, "John");
        assertEquals("J***", result);
    }

    @Test
    void testNullInputReturnsNull() {
        assertNull(SensitiveHandler.mask(SensitiveType.ChineseMobilePhone, null));
    }

    @Test
    void testBlankInputReturnsBlank() {
        assertEquals("", SensitiveHandler.mask(SensitiveType.ChineseMobilePhone, ""));
    }

    @Test
    void testMaskWithCustomPrefixSuffix() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseMobilePhone, "13812345678", 2, 3,
                s -> SensitiveHandler.maskWithIndex(s, 2, 3));
        assertEquals("138****5678", result);
    }

    @Test
    void testMaskWithCustomRegex() {
        String result = SensitiveHandler.mask(SensitiveType.Customer, "test123", "^[a-z0-9]+$", true,
                s -> SensitiveHandler.maskWithIndex(s, 1, 1));
        assertEquals("t*****3", result);
    }

    @Test
    void testMaskWithRegexMismatchThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                SensitiveHandler.mask(SensitiveType.Customer, "INVALID!!!", "^[a-z0-9]+$", true,
                        s -> s));
    }

    @Test
    void testMaskWithIndexTooShortThrows() {
        assertThrows(IllegalArgumentException.class, () ->
                SensitiveHandler.maskWithIndex("ab", 1, 1));
    }

    @Test
    void testMaskChineseTellPhone() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseTellPhone, "01012345678");
        assertNotNull(result);
        assertTrue(result.contains("****"));
    }

    @Test
    void testMaskCustomer() {
        String result = SensitiveHandler.mask(SensitiveType.Customer, "abcdef123456");
        assertEquals("a*********6", result);
    }

    @Test
    void testChineseNameSingleChar() {
        String result = SensitiveHandler.mask(SensitiveType.ChineseName, "赵");
        assertEquals("赵", result);
    }
}
