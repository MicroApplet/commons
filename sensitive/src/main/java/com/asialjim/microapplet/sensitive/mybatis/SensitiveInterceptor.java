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

package com.asialjim.microapplet.sensitive.mybatis;

import com.asialjim.microapplet.sensitive.SensitiveType;
import com.asialjim.microapplet.sensitive.annotation.Sensitive;
import com.asialjim.microapplet.sensitive.encrypt.EncryptionContextBean;
import com.asialjim.microapplet.sensitive.encrypt.EncryptionResult;
import com.asialjim.microapplet.sensitive.jackson.JacksonSensitiveHandler;
import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.plugin.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.*;

/**
 * MyBatis 敏感字段自动加解密拦截器
 * <p>实体类字段标记 {@code @Sensitive} 后，写入时自动加密、读出时自动解密，无需额外配置。</p>
 * <p>工作原理：</p>
 * <ul>
 *   <li>读操作：拦截 {@link ResultSetHandler#handleResultSets}，对返回结果中带 {@code @Sensitive} 的字段解密</li>
 *   <li>写操作：拦截 {@link ParameterHandler#setParameters}，对参数中带 {@code @Sensitive} 的字段加密</li>
 * </ul>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
        @Signature(type = ParameterHandler.class, method = "setParameters", args = {PreparedStatement.class})
})
public class SensitiveInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(SensitiveInterceptor.class);

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        Object target = invocation.getTarget();

        if (target instanceof ResultSetHandler) {
            Object result = invocation.proceed();
            decryptResult(result);
            return result;
        }

        if (target instanceof ParameterHandler handler) {
            Object param = handler.getParameterObject();
            if (Objects.nonNull(param))
                encryptParameter(param);
        }

        return invocation.proceed();
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    // ========== 解密（读） ==========

    private void decryptResult(Object result) {
        if (result instanceof List<?> list) {
            for (Object item : list) {
                if (Objects.nonNull(item))
                    decryptFields(item);
            }
        } else if (Objects.nonNull(result)) {
            decryptFields(result);
        }
    }

    private void decryptFields(Object obj) {
        Class<?> clazz = obj.getClass();
        for (Field field : getAllFields(clazz)) {
            Sensitive annotation = field.getAnnotation(Sensitive.class);
            if (Objects.isNull(annotation))
                continue;
            if (!String.class.equals(field.getType()))
                continue;

            field.setAccessible(true);
            try {
                String value = (String) field.get(obj);
                if (value == null || !value.startsWith("_mask"))
                    continue;

                String decrypted = EncryptionContextBean.instance.decrypt(value);
                field.set(obj, decrypted);
            } catch (Exception e) {
                log.warn("敏感字段解密失败: {}.{}", clazz.getSimpleName(), field.getName(), e);
            }
        }
    }

    // ========== 加密（写） ==========

    private void encryptParameter(Object param) {
        // 处理集合参数
        if (param instanceof Map<?, ?> map) {
            map.forEach((k,v) -> {
                if (Objects.nonNull(v))
                    encryptFields(v);
            });
            return;
        }
        encryptFields(param);
    }

    private void encryptFields(Object obj) {
        Class<?> clazz = obj.getClass();
        // 跳过基本类型和包装类
        if (clazz.getName().startsWith("java."))
            return;

        // 处理数组
        if (obj.getClass().isArray()) {
            for (Object item : (Object[]) obj) {
                encryptParameter(item);
            }
            return;
        }

        for (Field field : getAllFields(clazz)) {
            Sensitive annotation = field.getAnnotation(Sensitive.class);
            if (Objects.isNull(annotation))
                continue;
            if (!String.class.equals(field.getType()))
                continue;

            field.setAccessible(true);
            try {
                String value = (String) field.get(obj);
                if (value == null || value.isBlank())
                    continue;
                // 已加密则跳过
                if (value.startsWith("_mask"))
                    continue;
                EncryptionResult encrypted = EncryptionContextBean.instance.encrypt(value);
                String mask = JacksonSensitiveHandler.mask(annotation, value);
                // 合并: _mask|GM|nonce|encrypt|mac|脱敏文本
                String target = encrypted.withMask(mask);

                field.set(obj, target);
            } catch (Exception e) {
                log.warn("敏感字段加密失败: {}.{}", clazz.getSimpleName(), field.getName(), e);
            }
        }
    }

    // ========== 工具 ==========

    private static List<Field> getAllFields(Class<?> clazz) {
        List<Field> fields = new ArrayList<>();
        Class<?> current = clazz;
        while (current != null && !current.equals(Object.class)) {
            Collections.addAll(fields, current.getDeclaredFields());
            current = current.getSuperclass();
        }
        return fields;
    }
}
