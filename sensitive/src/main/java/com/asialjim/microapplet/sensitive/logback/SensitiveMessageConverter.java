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

package com.asialjim.microapplet.sensitive.logback;

import ch.qos.logback.classic.pattern.MessageConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.helpers.MessageFormatter;

import java.util.Arrays;
import java.util.List;

/**
 * Logback 敏感消息转换器。
 * <p>拦截 {@code %msg}，将非基本类型的参数通过 {@link JsonUtil} 序列化后再填充占位符，
 * 使被 {@code @Sensitive} 注解的字段自动脱敏。</p>
 *
 * <p>此转换器由 {@link SensitiveLogbackConfigurator} 自动注册，无需手动配置 logback.xml。</p>
 */
public class SensitiveMessageConverter extends MessageConverter {

    private static final List<Class<?>> PRIMITIVE_TYPES = Arrays.asList(
            String.class, Boolean.class, Character.class,
            Byte.class, Short.class, Integer.class, Long.class,
            Float.class, Double.class, Void.class
    );
    private static volatile boolean initialized = false;

    public SensitiveMessageConverter() {
        if (!initialized) {
            initialized = true;
        }
    }

    @Override
    public String convert(ILoggingEvent event) {
        Object[] args = event.getArgumentArray();
        if (args == null || args.length == 0)
            return event.getFormattedMessage();

        String message = event.getMessage();
        if (StringUtils.isBlank(message))
            return event.getFormattedMessage();

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

        return event.getFormattedMessage();
    }

    private static boolean isPrimitive(Object obj) {
        if (obj == null) return true;
        Class<?> clazz = obj.getClass();
        if (clazz.isPrimitive()) return true;
        return PRIMITIVE_TYPES.contains(clazz);
    }
}
