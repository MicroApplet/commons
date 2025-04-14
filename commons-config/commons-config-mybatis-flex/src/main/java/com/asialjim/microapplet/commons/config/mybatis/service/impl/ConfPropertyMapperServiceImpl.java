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

package com.asialjim.microapplet.commons.config.mybatis.service.impl;

import com.asialjim.microapplet.commons.config.cache.CacheName;
import com.asialjim.microapplet.commons.config.core.ConfProperty;
import com.asialjim.microapplet.commons.config.mybatis.mapper.ConfPropertyBaseMapper;
import com.asialjim.microapplet.commons.config.mybatis.po.ConfPropertyPo;
import com.asialjim.microapplet.commons.config.mybatis.service.ConfPropertyMapperService;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

/**
 * 通用配置信息数据服务
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Component
public class ConfPropertyMapperServiceImpl
        extends ServiceImpl<ConfPropertyBaseMapper, ConfPropertyPo>
        implements ConfPropertyMapperService {

    @Override
    @Cacheable(value = CacheName.Name.CONF_CACHE, key = "#type + ':' + #business + ':' + #code + ':' + #envCode")
    public TreeSet<ConfProperty> treeSetByTypeAndBusinessAndCodeAndEnv(String type, String business, String code, int envCode) {
        QueryChain<ConfPropertyPo> chain = queryChain();
        chain.where(ConfPropertyPo::getTp).eq(type);
        chain.where(ConfPropertyPo::getBiz).eq(business);
        chain.where(ConfPropertyPo::getName).eq(code);
        chain.where(ConfPropertyPo::getEnv).eq(envCode);
        List<ConfPropertyPo> list = chain.list();

        TreeSet<ConfProperty> res = new TreeSet<>(Comparator.comparingInt(ConfProperty::getVersion));
        list.stream().map(ConfPropertyPo::fromPo).forEach(res::add);
        return res;
    }

    @Override
    @CacheEvict(value = CacheName.Name.CONF_CACHE, key = "#source.type.name() + ':' + #source.business + ':' + #source.confCode + ':' + #source.env.getCode()")
    public void put(ConfProperty source) {
        source.setId(null);
        ConfPropertyPo po = ConfPropertyPo.toPo(source);
        boolean save = save(po);
        UpdateChain<ConfPropertyPo> chain = updateChain();
        chain.set(ConfPropertyPo::getEnable, Boolean.FALSE);
        chain.where(ConfPropertyPo::getTp).eq(po.getTp());
        chain.where(ConfPropertyPo::getBiz).eq(po.getBiz());
        chain.where(ConfPropertyPo::getName).eq(po.getName());
        chain.where(ConfPropertyPo::getEnv).eq(po.getEnv());
        chain.where(ConfPropertyPo::getVersion).ne(po.getVersion());
        boolean update = chain.update();
        log.info("配置：{}-{}-{}:{} 保存结果：{}，更新版本结果：{}", po.getTp(), po.getBiz(), po.getName(), po.getEnv(), save, update);
    }
}