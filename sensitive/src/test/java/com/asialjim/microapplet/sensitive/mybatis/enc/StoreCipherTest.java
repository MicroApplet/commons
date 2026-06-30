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

package com.asialjim.microapplet.sensitive.mybatis.enc;

import com.asialjim.microapplet.sensitive.encrypt.AlgorithmMode;
import com.asialjim.microapplet.sensitive.encrypt.AlgorithmModeConfig;
import com.asialjim.microapplet.sensitive.encrypt.SecretKeyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 存储加密体系单元测试（MODERN/AES 模式，无需 BouncyCastle）。
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
class StoreCipherTest {

    private StoreCipher cipher;

    /** 固定密钥仓库，保证测试可重复 */
    static class FixedKeyRepository implements SecretKeyRepository {
        @Override
        public Pair pairOf(AlgorithmMode mode) {
            byte[] enc = "0123456789abcdef".getBytes(StandardCharsets.UTF_8); // 16B -> AES-128
            byte[] mac = "fedcba9876543210fedcba9876543210".getBytes(StandardCharsets.UTF_8);
            return new Pair()
                    .setEncKey(new SecretKeySpec(enc, "AES"))
                    .setMacKey(new SecretKeySpec(mac, "HmacSHA256"));
        }
    }

    static class ModernModeConfig implements AlgorithmModeConfig {
        @Override
        public AlgorithmMode getCurrentMode() {
            return AlgorithmMode.MODERN;
        }
    }

    @BeforeAll
    static void setUpClass() {
        // MODERN 走 AES，JDK 自带，无需额外 Provider
    }

    @BeforeEach
    void setUp() {
        StoreKeyRepository keyRepository = new StoreKeyRepository(new FixedKeyRepository());
        StoreEncryptor encryptor = new StoreEncryptor(keyRepository);
        BlindIndexTokenizer bigramTokenizer = new BlindIndexTokenizer(keyRepository, BlindIndexGranularity.BIGRAM, 8);
        cipher = new StoreCipher(new ModernModeConfig(), encryptor, bigramTokenizer);
    }

    @Test
    void encrypt_isDeterministic() {
        System.out.println("==========================");
        System.out.println();
        String a = cipher.encrypt("110101199003078888");
        System.out.println("加密同明文  A    :    " + a);
        String b = cipher.encrypt("110101199003078888");
        System.out.println("加密同明文  B    :    " + b);
        assertEquals(a, b, "同一明文必须得到同一密文，否则唯一索引失效");
    }

    @Test
    void encrypt_differentPlaintext_differentCipher() {
        System.out.println("==========================");
        System.out.println();
        String a = cipher.encrypt("110101199003078888");
        System.out.println("加密不同明文  A    :    " + a);
        String b = cipher.encrypt("110101199003079999");
        System.out.println("加密不同明文  B    :    " + b);
        assertNotEquals(a, b);
    }

    @Test
    void encryptThenDecrypt_roundTrip() {
        System.out.println("==========================");
        System.out.println();
        String plain = "张三丰";
        String enc = cipher.encrypt(plain);
        System.out.println("加密      :    " + enc);
        assertTrue(cipher.isStoreCipher(enc));
        assertNotEquals(plain, enc);
        String decrypt = cipher.decrypt(enc);
        System.out.println("解密      :    " + decrypt);
        assertEquals(plain, decrypt);
    }

    @Test
    void encrypt_hasStorePrefix_andCompact() {
        System.out.println("==========================");
        System.out.println();
        String enc = cipher.encrypt("13800138000");
        System.out.println("加密      :    " + enc);
        assertTrue(enc.startsWith(StoreEncryptor.PREFIX));
        assertFalse(enc.contains("_mask"), "存储密文不应带传输方案的掩码信封");
    }

    @Test
    void encrypt_alreadyCipher_notDoubleEncrypted() {
        System.out.println("==========================");
        System.out.println();
        String once = cipher.encrypt("13800138000");
        System.out.println("第一次加密      :    " + once);
        String twice = cipher.encrypt(once);
        System.out.println("第二次加密      :    " + twice);
        assertEquals(once, twice, "已是存储密文不应被二次加密");
        assertEquals("13800138000", cipher.decrypt(twice));
    }

    @Test
    void encrypt_nullOrEmpty_passThrough() {
        assertNull(cipher.encrypt(null));
        assertEquals("", cipher.encrypt(""));
        assertNull(cipher.decrypt(null));
    }

    @Test
    void decrypt_plainText_passThrough() {
        System.out.println("==========================");
        System.out.println();
        // 非存储密文（无前缀）应原样返回，避免误解密
        assertEquals("not-a-cipher", cipher.decrypt("not-a-cipher"));
    }

    @Test
    void blindIndex_isDeterministic_andTokenized() {
        System.out.println("==========================");
        System.out.println();
        String idx1 = cipher.blindIndex("张三丰");
        String idx2 = cipher.blindIndex("张三丰");
        System.out.println("盲索引  1    :    " + idx1);
        System.out.println("盲索引  2    :    " + idx2);
        assertEquals(idx1, idx2);
        // bigram: 张三 / 三丰 两个 token，首尾含空格
        assertTrue(idx1.startsWith(" "));
        assertTrue(idx1.endsWith(" "));
        assertEquals(2, idx1.trim().split("\\s+").length);
    }

    @Test
    void blindIndex_supportsSubstringQuery() {
        System.out.println("==========================");
        System.out.println();
        String idx = cipher.blindIndex("张三丰");
        System.out.println("盲索引 张三丰  idx    :    " + idx);
        List<String> tokens = cipher.queryTokens("三丰");
        System.out.println("查询索引 三丰  tks    :    " + tokens);
        assertEquals(1, tokens.size());
        // 模拟 LIKE '%token%'
        assertTrue(idx.contains(tokens.getFirst().trim()),
                "查询子串的盲索引 token 应能命中存储的盲索引列");
    }

    @Test
    void blindIndex_queryMiss_whenSubstringNotPresent() {
        System.out.println("==========================");
        System.out.println();
        String idx = cipher.blindIndex("张三丰");
        System.out.println("盲索引 张三丰  idx    :    " + idx);
        List<String> tokens = cipher.queryTokens("李四");
        System.out.println("查询索引 李四  tks    :    " + tokens);
        assertFalse(tokens.isEmpty());
        boolean anyHit = tokens.stream().anyMatch(t -> idx.contains(t.trim()));
        assertFalse(anyHit, "不相关子串不应命中");
    }

    @Test
    void queryTokens_tooShort_returnsEmpty() {
        // bigram 粒度，单字符不足以构成 token
        assertTrue(cipher.queryTokens("张").isEmpty());
    }

    @Test
    void blindIndex_caseInsensitive() {
        System.out.println("==========================");
        System.out.println();
        String idx = cipher.blindIndex("AbCdef");
        System.out.println("盲索引  idx    :    " + idx);
        List<String> tokens = cipher.queryTokens("ABCDEF");
        System.out.println("查询索引  tks    :    " + tokens);
        boolean allHit = tokens.stream().allMatch(t -> idx.contains(t.trim()));
        assertTrue(allHit, "大小写规整后应能命中");
    }
}
