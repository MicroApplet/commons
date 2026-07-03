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

import com.asialjim.microapplet.sensitive.mybatis.enc.StoreCipher;
import com.asialjim.microapplet.sensitive.mybatis.enc.StoreEncrypt;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;

@AllArgsConstructor
public class SensitiveBeanCopyUtils {
    private static final Map<Class<?>, List<Field>> FIELD_LIST = new ConcurrentHashMap<>();
    private final StoreCipher storeCipher;

    public <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<?> constructor = clazz.getConstructor();
            //noinspection unchecked
            return (T) constructor.newInstance();
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException |
                 IllegalAccessException e) {
            return null;
        }
    }


    public <T> T decrypt(T source) {
        return wrap(source, storeCipher::decrypt, s -> StringUtils.EMPTY);
    }

    public <T> T encrypt(T source) {
        return wrap(source, storeCipher::encrypt, storeCipher::blindIndex);
    }

    private <T> T wrap(T source, Function<String, String> fun, Function<String, String> idxFun) {
        if (Objects.isNull(source) || Objects.isNull(fun))
            return source;
        if (source instanceof String str)
            //noinspection unchecked
            return (T) fun.apply(str);

        Class<?> aClass = source.getClass();

        // 跳过基本类型和包装类
        if (aClass.getName().startsWith("java."))
            return source;

        if (aClass.isArray()) {
            Object[] array = (Object[]) source;
            Object[] target = new Object[array.length];
            for (int i = 0; i < array.length; i++) {
                //noinspection unchecked
                target[i] = wrap((T)array[i], fun, idxFun);
            }
            //noinspection unchecked
            return (T) target;
        }

        //noinspection rawtypes
        if (source instanceof List list) {
            //noinspection rawtypes
            List target = new ArrayList<>(list.size());
            for (int i = 0; i < list.size(); i++) {
                Object src = list.get(i);
                Object wrap = wrap(src, fun, idxFun);
                //noinspection unchecked
                target.add(i, wrap);
            }
            //noinspection unchecked
            return (T) target;
        }

        //noinspection rawtypes
        if (source instanceof Map map) {
            //noinspection rawtypes
            Map target = new HashMap(map.size(), 1);
            //noinspection unchecked
            map.forEach((k, v) -> {
                Object wrap = wrap(v, fun, idxFun);
                //noinspection unchecked
                target.put(k, wrap);
            });
            //noinspection unchecked
            return (T) target;
        }

        //noinspection unchecked
        T target = (T) newInstance(aClass);
        if (Objects.isNull(target))
            return source;

        List<Field> fields = allFields(aClass);
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers) || Modifier.isAbstract(modifiers)) {
                continue;
            }
            //noinspection deprecation
            boolean can = field.isAccessible();
            try {
                field.setAccessible(true);
                Object o = field.get(source);
                Object exist = field.get(target);
                if (Objects.nonNull(exist))
                    continue;
                if (!(o instanceof String str)) {
                    field.set(target, o);
                    continue;
                }

                StoreEncrypt storeEncrypt = field.getAnnotation(StoreEncrypt.class);
                if (Objects.isNull(storeEncrypt)) {
                    field.set(target, o);
                    continue;
                }

                boolean searchable = storeEncrypt.searchable();
                if (searchable) {
                    String idxName = storeEncrypt.blindIndexField();
                    String name = field.getName() + idxName;
                    boolean accessible = false;
                    Field declaredField = null;
                    try {
                        declaredField = aClass.getDeclaredField(name);
                        //noinspection ConstantValue
                        if (Objects.nonNull(declaredField)) {
                            //noinspection deprecation
                            accessible = declaredField.isAccessible();
                            declaredField.setAccessible(true);
                            String token = idxFun.apply(str);
                            declaredField.set(target, token);
                        }
                    } catch (NoSuchFieldException e) {
                        throw new IllegalStateException("类" + aClass.getName() + "找不到盲索引字段：" + name);
                    } finally {
                        if (Objects.nonNull(declaredField))
                            declaredField.setAccessible(accessible);
                    }
                }

                String enc = fun.apply(str);
                field.set(target, enc);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } finally {
                field.setAccessible(can);
            }
        }

        return target;
    }


    private List<Field> allFields(Class<?> clazz) {
        return FIELD_LIST.computeIfAbsent(clazz, type -> {
            List<Field> fields = new ArrayList<>();
            Class<?> current = type;
            while (current != null && !current.equals(Object.class)) {
                Collections.addAll(fields, current.getDeclaredFields());
                current = current.getSuperclass();
            }
            return fields;
        });
    }
}