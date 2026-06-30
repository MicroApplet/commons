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
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 盲索引器：把明文切成 n-gram，每个 gram 经 HMAC 截断成定长 token。
 *
 * <p>用于模糊查询：写入时把明文 token 化存入盲索引列，查询时把查询子串同样 token 化后做 {@code LIKE} 匹配。</p>
 *
 * <p>设计要点：</p>
 * <ul>
 *     <li>token 单向不可逆（HMAC），不泄露明文，且随存储密钥派生。</li>
 *     <li>token 定长（默认 8 字符 base32url），存储有界。</li>
 *     <li>截断带来少量碰撞（假阳性），命中候选行需解密后再精确过滤。</li>
 *     <li>token 间以空格分隔，便于 {@code LIKE '% token %'} 或全文索引。</li>
 * </ul>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public class BlindIndexTokenizer {
    private static final String HMAC = "HmacSHA256";
    /** token 前后补空格，保证 LIKE '% xxx %' 不会跨 token 误匹配 */
    private static final char SEP = ' ';

    private final StoreKeyRepository keyRepository;
    private final BlindIndexGranularity granularity;
    private final int tokenLength;

    public BlindIndexTokenizer(StoreKeyRepository keyRepository,
                               BlindIndexGranularity granularity,
                               int tokenLength) {
        this.keyRepository = keyRepository;
        this.granularity = granularity;
        this.tokenLength = tokenLength;
    }

    /**
     * 对明文构建盲索引列内容。
     *
     * @return 形如 {@code " a3f9c1 7b2e08 "}，首尾含空格便于边界匹配；明文为空返回空串
     */
    public String index(AlgorithmMode mode, String plaintext) {
        String norm = normalize(plaintext);
        if (StringUtils.isEmpty(norm))
            return StringUtils.EMPTY;

        byte[] key = keyRepository.keysOf(mode).indexKey();
        List<String> grams = grams(norm);
        Set<String> tokens = new LinkedHashSet<>();
        for (String gram : grams) {
            tokens.add(token(key, gram));
        }
        return SEP + String.join(String.valueOf(SEP), tokens) + SEP;
    }

    /**
     * 把查询子串转成用于 LIKE 的 token 片段列表。
     * <p>调用方对每个片段做 {@code blindIndexColumn LIKE CONCAT('%', :token, '%')} 并 AND 连接。</p>
     *
     * @return 每个元素是带前后空格的单 token，如 {@code " a3f9c1 "}；查询串过短返回空列表
     */
    public List<String> queryTokens(AlgorithmMode mode, String keyword) {
        String norm = normalize(keyword);
        List<String> result = new ArrayList<>();
        if (StringUtils.isEmpty(norm) || norm.length() < granularity.n())
            return result;

        byte[] key = keyRepository.keysOf(mode).indexKey();
        for (String gram : grams(norm)) {
            result.add(SEP + token(key, gram) + SEP);
        }
        return result;
    }

    private List<String> grams(String text) {
        int n = granularity.n();
        List<String> grams = new ArrayList<>();
        if (text.length() < n) {
            grams.add(text);
            return grams;
        }
        for (int i = 0; i + n <= text.length(); i++) {
            grams.add(text.substring(i, i + n));
        }
        return grams;
    }

    private String token(byte[] key, String gram) {
        try {
            Mac mac = Mac.getInstance(HMAC);
            mac.init(new SecretKeySpec(key, HMAC));
            byte[] full = mac.doFinal(gram.getBytes(StandardCharsets.UTF_8));
            String b64 = Base64.getUrlEncoder().withoutPadding().encodeToString(full);
            return b64.substring(0, Math.min(tokenLength, b64.length()));
        } catch (Exception e) {
            throw new IllegalStateException("盲索引 token 生成失败", e);
        }
    }

    /** 规整化：去首尾空白 + 统一小写，降低大小写/空白造成的漏匹配 */
    private String normalize(String text) {
        if (text == null)
            return StringUtils.EMPTY;
        return text.strip().toLowerCase();
    }
}
