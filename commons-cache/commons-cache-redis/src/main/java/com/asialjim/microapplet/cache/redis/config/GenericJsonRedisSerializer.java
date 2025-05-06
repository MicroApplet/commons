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

package com.asialjim.microapplet.cache.redis.config;


import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.jsontype.impl.StdTypeResolverBuilder;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.KotlinDetector;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.util.ClassUtils;

/**
 * 基于泛型的JsonRedis序列化组件
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/5/6, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class GenericJsonRedisSerializer extends GenericJackson2JsonRedisSerializer {

    public GenericJsonRedisSerializer(ObjectMapper mapper, String classPropertyTypeName) {
        super(mapper);
        registerNullValueSerializer(mapper, StringUtils.isNotBlank(classPropertyTypeName) ? classPropertyTypeName : "@class");

        StdTypeResolverBuilder typer = new TypeResolverBuilder(ObjectMapper.DefaultTyping.EVERYTHING, mapper.getPolymorphicTypeValidator());
        typer = typer.init(JsonTypeInfo.Id.CLASS, null);
        typer = typer.inclusion(JsonTypeInfo.As.PROPERTY);
        if (StringUtils.isNotBlank(classPropertyTypeName))
            typer = typer.typeProperty(classPropertyTypeName);

        mapper.setDefaultTyping(typer);
    }


    private static class TypeResolverBuilder extends ObjectMapper.DefaultTypeResolverBuilder {
        public TypeResolverBuilder(ObjectMapper.DefaultTyping t, PolymorphicTypeValidator ptv) {
            super(t, ptv);
        }

        public ObjectMapper.DefaultTypeResolverBuilder withDefaultImpl(Class<?> defaultImpl) {
            return this;
        }

        public boolean useForType(JavaType t) {
            if (t.isJavaLangObject()) {
                return true;
            } else {
                t = this.resolveArrayOrWrapper(t);
                if (!t.isEnumType() && !ClassUtils.isPrimitiveOrWrapper(t.getRawClass())) {
                    if (t.isFinal() && !KotlinDetector.isKotlinType(t.getRawClass()) && t.getRawClass().getPackage().getName().startsWith("java")) {
                        return false;
                    } else {
                        return !TreeNode.class.isAssignableFrom(t.getRawClass());
                    }
                } else {
                    return false;
                }
            }
        }

        private JavaType resolveArrayOrWrapper(JavaType type) {
            while (type.isArrayType()) {
                type = type.getContentType();
                if (type.isReferenceType()) {
                    type = this.resolveArrayOrWrapper(type);
                }
            }

            while (type.isReferenceType()) {
                type = type.getReferencedType();
                if (type.isArrayType()) {
                    type = this.resolveArrayOrWrapper(type);
                }
            }

            return type;
        }
    }
}