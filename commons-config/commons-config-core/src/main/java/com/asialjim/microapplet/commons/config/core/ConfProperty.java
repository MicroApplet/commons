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

package com.asialjim.microapplet.commons.config.core;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 配置信息数据转换对象
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
public class ConfProperty implements Serializable {
    @Serial
    private static final long serialVersionUID = -4020732656042712641L;

    /**
     * 配置编号
     */
    private String id;
    /**
     * 配置类型
     */
    private ConfType type;
    /**
     * 业务命名空间
     */
    private String business;
    /**
     * 业务命名空间名
     */
    private String businessName;
    /**
     * 配置编码
     */
    private String confCode;
    /**
     * 配置名称：中文名
     */
    private String confCodeName;
    /**
     * 配置数据
     */
    private String value;
    /**
     * 配置环境
     */
    private Env env;
    /**
     * 配置描述
     */
    private String describe;
    /**
     * 配置备注
     */
    private String remark;
    /**
     * 配置版本号
     */
    private Integer version;
    /**
     * 是否启用配置
     */
    private Boolean enable;
    /**
     * 配置创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    /**
     * 配置更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    public static <T> T confInstance(ConfProperty property, Class<T> typeClass) {
        if (Objects.isNull(property) || Objects.isNull(typeClass))
            return null;
        String value = property.getValue();
        if (StringUtils.isBlank(value))
            return null;

        return jsonToBean(value, typeClass);
    }

    private static final JsonMapper JSON_MAPPER = new JsonMapper();

    static {
        JSON_MAPPER.registerModules(new JavaTimeModule());
    }

    private static String toJson(Object bean) {
        try {
            return JSON_MAPPER.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T jsonToBean(String json, Class<T> beanClass) {
        try {
            return JSON_MAPPER.readValue(json, beanClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String value(Conf<?> conf) {
        return toJson(conf);
    }

    public boolean contentAndEnvEquals(ConfProperty that) {
        if  (Objects.isNull(that))
            return false;
        return
        Objects.equals(type,that.type)
                && Objects.equals(business,that.business)
                && Objects.equals(this.confCode,that.confCode)
                && Objects.equals(this.env,that.env)
                && Objects.equals(this.value,that.value);
    }
}