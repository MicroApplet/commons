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

package com.asialjim.microapplet.sensitive.jackson;

import com.asialjim.microapplet.sensitive.annotation.Sensitive;
import com.asialjim.microapplet.sensitive.encrypt.EncryptionContextBean;
import com.asialjim.microapplet.sensitive.encrypt.EncryptionResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonGenerator;
import tools.jackson.databind.BeanProperty;
import tools.jackson.databind.SerializationContext;
import tools.jackson.databind.ValueSerializer;
import tools.jackson.databind.ser.std.StdSerializer;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 敏感数据序列化器
 * <p>序列化时对 {@link Sensitive} 字段执行：脱敏 → 加密 → 合并为 {@code _mask|...|脱敏文本} 格式。</p>
 */
@Slf4j
public class SensitiveSerializer extends StdSerializer<String> {
    private final Sensitive sensitive;

    public SensitiveSerializer() {
        super(String.class);
        this.sensitive = null;
    }

    public SensitiveSerializer(Sensitive sensitive) {
        super(String.class);
        this.sensitive = sensitive;
    }

    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializationContext ctxt) throws JacksonException {
        if (StringUtils.isBlank(s) || Objects.isNull(this.sensitive)) {
            jsonGenerator.writeString(s);
            return;
        }

        // 脱敏
        String mask = JacksonSensitiveHandler.mask(sensitive, s);
        // 加密
        EncryptionResult encrypt = EncryptionContextBean.instance.encrypt(s);
        // 合并: _mask|GM|nonce|encrypt|mac|脱敏文本
        String target = encrypt.withMask(mask);
        jsonGenerator.writeString(target);
    }

    @Override
    public ValueSerializer<?> createContextual(SerializationContext serializationContext, BeanProperty beanProperty) {
        if (log.isDebugEnabled())
            log.info("SensitiveSerializer.createContextual called for property: {}", beanProperty != null ? beanProperty.getName() : "null");
        if (Objects.isNull(beanProperty))
            return this;
        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        if (log.isDebugEnabled())
            log.info("Annotation from beanProperty: {}", annotation);
        if (Objects.isNull(annotation))
            annotation = getFieldAnnotation(beanProperty);
        if (log.isDebugEnabled())
            log.info("Annotation from field: {}", annotation);
        if (Objects.isNull(annotation))
            return this;
        return new SensitiveSerializer(annotation);
    }

    private static Sensitive getFieldAnnotation(BeanProperty beanProperty) {
        if (Objects.isNull(beanProperty.getMember()))
            return null;
        Class<?> clazz = beanProperty.getMember().getDeclaringClass();
        String name = beanProperty.getName();
        try {
            Field field = clazz.getDeclaredField(name);
            return field.getAnnotation(Sensitive.class);
        } catch (NoSuchFieldException e) {
            return null;
        }
    }
}
