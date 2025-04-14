/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.commons.config.core;

import com.asialjim.microapplet.common.application.AppStarted;
import com.asialjim.microapplet.common.event.BaseAsyncListener;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

/**
 * 应用启动后，自动注册配置信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class AppStartedListener4ConfProperties extends BaseAsyncListener<AppStarted> {
    private ConfPropertyRepository confPropertyRepository;
    private List<Conf<?>> confList;

    @Autowired(required = false)
    public void setConfPropertyRepository(ConfPropertyRepository confPropertyRepository) {
        this.confPropertyRepository = confPropertyRepository;
    }

    @Autowired(required = false)
    public void setConfList(List<Conf<?>> confList) {
        this.confList = confList;
    }

    @Override
    public void doOnEvent(AppStarted event) {
        if (CollectionUtils.isEmpty(confList))
            return;
        for (Conf<?> conf : confList) {
            exe(() -> register(conf));
        }
    }

    private void register(Conf<?> conf) {
        if (Objects.isNull(conf))
            return;
        if (Objects.isNull(this.confPropertyRepository))
            return;
        Class<?> confClass = conf.deftConf().getClass();
        TypedConfiguration annotation = confClass.getAnnotation(TypedConfiguration.class);
        if (Objects.isNull(annotation))
            return;

        this.confPropertyRepository.register(annotation, conf);
    }
}