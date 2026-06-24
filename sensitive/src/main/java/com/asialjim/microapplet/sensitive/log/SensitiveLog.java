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

package com.asialjim.microapplet.sensitive.log;

import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * 敏感日志格式化工具
 * <p>纯 SLF4J 实现，不依赖 Logback 或 Spring。</p>
 * <p>将参数中的 Java 对象通过 Jackson 序列化（触发 {@code @Sensitive} 注解），
 * 使日志输出中的敏感字段自动脱敏。</p>
 *
 * <pre>{@code
 * import static com.asialjim.microapplet.sensitive.log.SensitiveLog.fmt;
 *
 * User user = ...;
 * log.info(fmt("user: {}", user));
 * // 输出: user: {"name":"张**","phone":"138****1234"}
 * }</pre>
 */
public final class SensitiveLog {

    private static final List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(
            String.class, Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Void.class
    );


    private SensitiveLog() {}

    /**
     * 格式化日志消息，将非基本类型的参数通过 Jackson 序列化后再填充占位符。
     *
     * @param message SLF4J 格式的消息（支持 {} 占位符）
     * @param args    参数列表
     * @return 格式化后的字符串
     */
    public static String fmt(String message, Object... args) {
        if (args == null || args.length == 0)
            return message;

        Object[] processed = new Object[args.length];
        boolean changed = false;

        for (int i = 0; i < args.length; i++) {
            if (args[i] == null || isPrimitive(args[i])) {
                processed[i] = args[i];
            } else {
                try {
                    processed[i] = JsonUtil.instance.toStr(args[i]);
                    changed = true;
                } catch (Exception e) {
                    processed[i] = args[i];
                }
            }
        }

        if (changed)
            return MessageFormatter.arrayFormat(message, processed).getMessage();

        return MessageFormatter.arrayFormat(message, args).getMessage();
    }

    private static boolean isPrimitive(Object obj) {
        if (obj == null)
            return true;
        Class<?> clazz = obj.getClass();
        if (clazz.isPrimitive())
            return true;
        return PRIMITIVE_TYPES.contains(clazz);
    }
}
