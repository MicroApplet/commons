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
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.StreamReadFeature;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Locale;
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
    //protected static final DateTimeFormatter DATE_TIME_FORMATTER =  DateTimeFormatter.ISO_LOCAL_DATE_TIME;

   /*
    protected static final DateTimeFormatter DATE_TIME_FORMATTER = new DateTimeFormatterBuilder()
            .appendPattern("yyyy-MM-dd[ [HH][:mm][:ss]]")
            .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
            .appendZoneId()
            .toFormatter();
    */

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

        // 配置序列化特性
        configureSerializationFeatures(mapper);

        // 配置反序列化特性
        configureDeserializationFeatures(mapper);

        // 配置日期时间处理
        configureDateTimeHandling(mapper);

        // 配置序列化包含规则
        configureSerializationInclusion(mapper);

        return mapper;
    }

    private static void configureSerializationFeatures(ObjectMapper mapper) {
        mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private static void configureDeserializationFeatures(ObjectMapper mapper) {
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.enable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        mapper.disable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
        mapper.enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION.mappedFeature());
    }

    private static void configureDateTimeHandling(ObjectMapper mapper) {
        // 设置传统日期格式
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        // 配置JavaTime模块
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置日期时间序列化/反序列化
       /*
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
        DateTimeFormatter timeFormatter = DateTimeFormatter.ISO_LOCAL_TIME;
        */

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(("HH:mm:ss"));


        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(dateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(dateTimeFormatter));

        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(dateFormatter));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(dateFormatter));

        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(timeFormatter));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(timeFormatter));

        mapper.registerModule(javaTimeModule);
    }


    private static void configureSerializationInclusion(ObjectMapper mapper) {
        // 注意：setSerializationInclusion 会覆盖前一个设置
        // 所以合并为一次设置，优先使用 NON_EMPTY
        mapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
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