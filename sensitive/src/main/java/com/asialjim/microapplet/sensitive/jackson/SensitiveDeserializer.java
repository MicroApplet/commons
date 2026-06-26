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
import org.apache.commons.lang3.StringUtils;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.*;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * 敏感数据反序列化器
 * <p>反序列化时：若为 {@code _mask|...} 密文格式则解密还原，若为明文则校验正则。</p>
 */
public class SensitiveDeserializer extends ValueDeserializer<String> {
    private final Sensitive sensitive;

    public SensitiveDeserializer() {
        this(null);
    }

    public SensitiveDeserializer(Sensitive sensitive) {
        this.sensitive = sensitive;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws JacksonException {
        String value = jsonParser.getValueAsString();
        if (StringUtils.isBlank(value) || Objects.isNull(this.sensitive))
            return value;

        // 密文格式 → 解密
        if (EncryptionResult.isEncryptionMaskData(value))
            return EncryptionContextBean.instance.decrypt(value);

        // 明文字段 → 校验正则
        if (JacksonSensitiveHandler.match(value, this.sensitive))
            return value;

        throw new IllegalArgumentException("敏感数据校验失败:不符合校验规则");
    }

    @Override
    public ValueDeserializer<?> createContextual(DeserializationContext deserializationContext, BeanProperty beanProperty) {
        if (Objects.isNull(beanProperty))
            return this;
        Sensitive annotation = beanProperty.getAnnotation(Sensitive.class);
        if (Objects.isNull(annotation))
            annotation = getFieldAnnotation(beanProperty);
        if (Objects.isNull(annotation))
            return this;
        return new SensitiveDeserializer(annotation);
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
