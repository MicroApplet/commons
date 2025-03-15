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

package io.github.microapplet.commons.config.core;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;

/**
 * 配置信息仓库
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface ConfPropertyRepository {

    /**
     * 查询配置模板
     *
     * @param type     {@link ConfType type}
     * @param business {@link String business}
     * @param code     {@link String code}
     * @return {@link TreeSet<ConfProperty> }
     * @since 2025/3/10
     */
    default TreeSet<ConfProperty> template(ConfType type, String business, String code) {
        return query(type, business, code, Env.Init);
    }

    /**
     * 查询指定配置的所有环境的所有版本
     *
     * @param type     {@link ConfType type}
     * @param business {@link String business}
     * @param code     {@link String code}
     * @return {@link TreeSet<ConfProperty>}
     * @since 2025/3/10
     */
    default TreeSet<ConfProperty> query(ConfType type, String business, String code) {
        return query(type, business, code, null);
    }

    /**
     * 根据配置类型、业务类型、配置编码、环境编号查询所有配置信息，并根据版本号排序
     *
     * @param type     {@link ConfType 类型}
     * @param business {@link String 业务命名空间}
     * @param code     {@link String 配置编码}
     * @param env      {@link Env 系统环境}
     * @return {@link TreeSet<ConfProperty> }
     * @since 2025/3/10
     */
    TreeSet<ConfProperty> query(ConfType type, String business, String code, Env env);

    default ConfProperty queryEnable(ConfType type, String business, String code, Env env) {
        ConfProperty target = doQueryEnable(type, business, code, env);
        if (Objects.nonNull(target))
            return target;
        if (!env.hasNext())
            return null;
        return queryEnable(type, business, code, env.next());
    }

    default ConfProperty doQueryEnable(ConfType type, String business, String code, Env env) {
        TreeSet<ConfProperty> queries = query(type, business, code, env);
        if (CollectionUtils.isNotEmpty(queries)) {
            return queries.stream()
                    .sorted(Comparator.comparingInt(ConfProperty::getVersion))
                    .filter(item -> Boolean.TRUE.equals(item.getEnable()))
                    .findFirst().orElse(null);
        }
        return null;
    }

    void put(ConfProperty confProperty);

    /**
     * 注册配置信息
     *
     * @param annotation {@link TypedConfiguration annotation}
     * @param conf       {@link Conf conf}
     * @since 2025/3/10
     */
    default void register(TypedConfiguration annotation, Conf<?> conf) {
        if (Objects.isNull(annotation) || Objects.isNull(conf))
            return;
        // 注册配置模板：初始化配置
        register(annotation, conf.deftConf(), Env.Init);
        Map<Env, ? extends Conf<?>> configs = conf.configs();
        if (MapUtils.isEmpty(configs))
            return;

        // 分环境注册配置
        for (Map.Entry<Env, ? extends Conf<?>> entry : configs.entrySet()) {
            Env key = entry.getKey();
            Conf<?> value = entry.getValue();

            register(annotation, value, key);
        }
    }

    default void register(TypedConfiguration annotation, Conf<?> target, Env env) {
        ConfProperty property = new ConfProperty();
        property.setType(annotation.type());
        property.setBusiness(annotation.business());
        property.setBusinessName(annotation.businessName());
        property.setConfCode(annotation.code());
        property.setConfCodeName(annotation.codeName());
        property.setValue(ConfProperty.value(target));
        property.setEnv(env);
        property.setDescribe(annotation.desc());
        property.setRemark(annotation.remark());
        property.setEnable(true);
        property.setCreateTime(LocalDateTime.now());
        property.setUpdateTime(LocalDateTime.now());
        TreeSet<ConfProperty> exists = query(annotation.type(), annotation.business(), annotation.code(), env);
        if (CollectionUtils.isNotEmpty(exists)) {
            ConfProperty last = exists.last();
            boolean contentEquals = property.contentAndEnvEquals(last);
            if (contentEquals)
                return;
        }
        int version = CollectionUtils.size(exists) + 1;
        property.setVersion(version);
        put(property);
    }
}