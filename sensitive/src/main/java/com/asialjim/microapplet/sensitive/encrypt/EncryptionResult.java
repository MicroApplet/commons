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

package com.asialjim.microapplet.sensitive.encrypt;

import org.apache.commons.lang3.StringUtils;

import java.util.Base64;

public record EncryptionResult(
        AlgorithmMode algorithmMode,
        byte[] nonce,
        byte[] encrypt,
        byte[] mac) {

    public String toFormattedString() {
        return String.format("_mask|%s|%s|%s|%s|",
                algorithmMode.getCode(),
                bytesToBase64Url(nonce),
                bytesToBase64Url(encrypt),
                bytesToBase64Url(mac));
    }

    public String withMask(String mask) {
        return toFormattedString() + mask;
    }

    public static boolean isEncryptionMaskData(String source) {
        if (StringUtils.isBlank(source))
            return false;

        return "_mask".startsWith(source) && StringUtils.split(source, "\\|").length == 6;
    }

    public static EncryptionResult fromFormattedString(String formattedString) {
        String[] parts = formattedString.split("\\|");
        if (parts.length != 6 || !"_mask".equals(parts[0]))
            throw new IllegalArgumentException("无效的数据格式");

        AlgorithmMode mode = AlgorithmMode.fromCode(parts[1]);
        return new EncryptionResult(mode, base64UrlToBytes(parts[2]), base64UrlToBytes(parts[3]), base64UrlToBytes(parts[4]));
    }

    private static String bytesToBase64Url(byte[] bytes) {
        if (bytes == null) return "";
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    private static byte[] base64UrlToBytes(String str) {
        if (StringUtils.isBlank(str)) return new byte[0];
        return Base64.getUrlDecoder().decode(str);
    }

    @Override
    @SuppressWarnings("NullableProblems")
    public String toString() {
        return "EncryptionResult => " + toFormattedString();
    }
}
