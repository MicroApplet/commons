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

package com.asialjim.microapplet.commons.config.mybatis.service;

import com.asialjim.microapplet.commons.config.core.ConfProperty;
import com.asialjim.microapplet.commons.config.mybatis.po.ConfPropertyPo;
import com.mybatisflex.core.service.IService;

import java.util.TreeSet;

/**
 * 通用配置信息存储服务
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface ConfPropertyMapperService extends IService<ConfPropertyPo> {
    /**
     * 查询配置信息：并按照版本号排序
     *
     * @param name     {@link String name}
     * @param business {@link String business}
     * @param code     {@link String code}
     * @param envCode  {@link int envCode}
     * @return {@link TreeSet<ConfProperty>}
     * @since 2025/4/14
     */
    TreeSet<ConfProperty> treeSetByTypeAndBusinessAndCodeAndEnv(String name, String business, String code, int envCode);

    /**
     * 注册：更新配置
     *
     * @param confProperty {@link ConfProperty confProperty}
     * @since 2025/4/14
     */
    void put(ConfProperty confProperty);
}