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

package io.github.microapplet.common.utils;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;

/**
 * 密码存储工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/13, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class PasswordStorage {

    // 安全参数配置
    private static final int ITERATIONS = 1<<16;    // 迭代次数 (JDK8最高支持约2^16)
    private static final int SALT_BYTES = 16;       // 盐值长度
    private static final int HASH_BYTES = 32;        // 哈希输出长度
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";

    public static String createHash(String password){
        return createHash(password.toCharArray());
    }

    /**
     * 生成安全密码哈希
     *
     * @param password 原始密码（使用后会被清空）
     * @return 格式：算法$迭代次数$盐$哈希
     */
    public static String createHash(char[] password) {
        PBEKeySpec spec = null;
        try {
            // 生成加密安全随机盐
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            byte[] salt = new byte[SALT_BYTES];
            random.nextBytes(salt);

            // 生成密钥规格
            spec = new PBEKeySpec(password, salt, ITERATIONS, HASH_BYTES * 8);

            // 计算哈希
            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] hash = skf.generateSecret(spec).getEncoded();

            // 格式：算法$迭代次数$盐$哈希
            return String.format("pbkdf2_sha256$%d$%s$%s",
                    ITERATIONS,
                    Base64.getUrlEncoder().withoutPadding().encodeToString(salt),
                    bytesToHex(hash));
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        } finally {
            if (spec != null) {
                spec.clearPassword();
            }
        }
    }

    /**
     * 验证密码正确度
     *
	 * @param password {@link String password}
	 * @param correctHash {@link String correctHash}
     * @return {@link boolean }
     * @since 2025/3/13
     */
    public static boolean verifyPassword(String password, String correctHash){
        return verifyPassword(password.toCharArray(), correctHash);
    }

    /**
     * 验证密码是否正确
     *
     * @param password    待验证密码
     * @param correctHash 存储的正确哈希
     * @return 是否匹配
     */
    public static boolean verifyPassword(char[] password, String correctHash) {
        try {
            // 解析存储的哈希
            String[] parts = correctHash.split("\\$");
            if (parts.length != 4)
                throw new IllegalArgumentException("Invalid hash format");

            int iterations = Integer.parseInt(parts[1]);
            byte[] salt = Base64.getUrlDecoder().decode(parts[2]);
            byte[] originalHash = hexToBytes(parts[3]);

            // 使用相同参数计算哈希
            PBEKeySpec spec = new PBEKeySpec(
                    password,
                    salt,
                    iterations,
                    originalHash.length * 8
            );

            SecretKeyFactory skf = SecretKeyFactory.getInstance(ALGORITHM);
            byte[] testHash = skf.generateSecret(spec).getEncoded();

            // 安全比较哈希值
            return slowEquals(originalHash, testHash);
        } catch (Exception e) {
            return false;
        } finally {
            Arrays.fill(password, '\0');  // 清空密码内存
        }
    }

    /**
     * 安全比较字节数组（恒定时间）
     */
    private static boolean slowEquals(byte[] a, byte[] b) {
        int diff = a.length ^ b.length;
        for (int i = 0; i < a.length && i < b.length; i++) {
            diff |= a[i] ^ b[i];
        }
        return diff == 0;
    }

    /**
     * 字节数组转十六进制字符串（JDK8兼容）
     */
    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = "0123456789abcdef".charAt(v >>> 4);
            hexChars[j * 2 + 1] = "0123456789abcdef".charAt(v & 0x0F);
        }
        return new String(hexChars);
    }

    /**
     * 十六进制字符串转字节数组（JDK8兼容）
     */
    private static byte[] hexToBytes(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    /*public static void main(String[] args) {

        // 生成哈希
        String hash = createHash("mySecretPassword");
        System.out.println("Stored Hash: " + hash);

        // 验证正确密码
        boolean isValid = verifyPassword("mySecretPassword", hash);
        System.out.println("Password valid: " + isValid);

        // 验证错误密码
        boolean isInvalid = verifyPassword("annotherpassword", hash);
        System.out.println("Wrong password valid: " + isInvalid);
    }*/
}