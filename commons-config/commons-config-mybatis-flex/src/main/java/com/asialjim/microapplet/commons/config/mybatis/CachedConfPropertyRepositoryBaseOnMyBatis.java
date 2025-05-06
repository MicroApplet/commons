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

package com.asialjim.microapplet.commons.config.mybatis;

import com.asialjim.microapplet.commons.config.core.*;
import com.asialjim.microapplet.commons.config.mybatis.po.ConfPropertyPo;
import com.asialjim.microapplet.commons.config.mybatis.service.ConfPropertyMapperService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * 基于泛型的通用配置信息仓库
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class CachedConfPropertyRepositoryBaseOnMyBatis implements ConfPropertyRepository {
    @Resource
    private ConfPropertyMapperService confPropertyMapperService;

    @Override
    public TreeSet<ConfProperty> query(ConfType type, String business, String code, Env env) {
         List<ConfPropertyPo> res = this.confPropertyMapperService.treeSetByTypeAndBusinessAndCodeAndEnv(type.name(), business, code, env.getCode());
        if (CollectionUtils.isNotEmpty(res)){
            TreeSet<ConfProperty> target = new TreeSet<>(Comparator.comparingInt(ConfProperty::getVersion));
            res.stream().map(ConfPropertyPo::fromPo).forEach(target::add);
            return target;
        }

        if (env.hasNext())
            return query(type, business, code, env.next());

        return new TreeSet<>(Comparator.comparingInt(ConfProperty::getVersion));
    }

    @Override
    public void put(ConfProperty source) {
        source.setEnable(true);

        ConfType type = source.getType();
        String business = source.getBusiness();
        String name = source.getConfCode();
        Env env = source.getEnv();

        TreeSet<ConfProperty> exist = query(type, business, name, env);
        Integer version = CollectionUtils.size(exist) + 1;
        source.setVersion(version);

        if (CollectionUtils.isNotEmpty(exist)) {
            ConfProperty last = exist.last();
            boolean contentEquals = source.contentAndEnvEquals(last);
            if (contentEquals)
                return;
        }

        this.confPropertyMapperService.put(source);
    }
}