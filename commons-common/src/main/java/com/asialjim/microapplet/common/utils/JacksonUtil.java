/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.common.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 基于jackson的工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/7, &nbsp;&nbsp; <em>version:1.0</em>
 */
public abstract class JacksonUtil {
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ [HH][:mm][:ss]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .appendZoneId()
            .toFormatter();

    public static JacksonUtil instance(ObjectMapper mapper){
        return new JacksonUtil() {

            @Override
            protected ObjectMapper objectMapper() {
                return mapper;
            }
        };
    }

    public static ObjectMapper init(ObjectMapper mapper) {
        if (Objects.isNull(mapper))
            return null;
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());
        mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        JavaTimeModule timeModule = new JavaTimeModule();
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(DATE_TIME_FORMATTER);
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(DATE_TIME_FORMATTER);
        timeModule.addDeserializer(LocalDateTime.class, localDateTimeDeserializer);
        timeModule.addSerializer(LocalDateTime.class, localDateTimeSerializer);
        mapper.registerModule(timeModule);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        return mapper;
    }

    protected abstract ObjectMapper objectMapper();

    public final JavaType constructType(Type type) {
        return objectMapper().getTypeFactory().constructType(type);
    }

    public final JavaType constructParameterizedType(Class<?> rawType, Class<?>... args) {
        return objectMapper().getTypeFactory().constructParametricType(rawType, args);
    }

    public final String toStr(Object bean) {
        try {
            return objectMapper().writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(byte[] is, JavaType type){
        try {
            return objectMapper().readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(InputStream is, JavaType type) {
        try {
            return objectMapper().readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(InputStream is, Class<T> type) {
        try {
            return objectMapper().readValue(is, type);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(String str, Class<T> type) {
        try {
            return objectMapper().readValue(str, type);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(String str, TypeReference<T> type) {
        try {
            return objectMapper().readValue(str, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T toBean(String str, JavaType javaType) {
        try {
            return objectMapper().readValue(str, javaType);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> Map<String, T> toMap(String str, Class<T> valueType) {
        JavaType javaType = constructParameterizedType(Map.class, String.class, valueType);
        return toBean(str, javaType);
    }

    public final <T> List<T> toList(String str, Class<T> listType) {
        JavaType javaType = constructParameterizedType(List.class, listType);
        return toBean(str, javaType);
    }
}