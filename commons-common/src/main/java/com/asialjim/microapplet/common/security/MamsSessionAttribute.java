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

package com.asialjim.microapplet.common.security;


import com.asialjim.microapplet.common.context.Res;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.util.Objects;
import java.util.Optional;

/**
 * 获取当前会话属性接口
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface MamsSessionAttribute extends Ordered {

    MamsSession currentSession();

    default String currentUserid(){
        return Optional.ofNullable(currentSession()).map(MamsSession::getUserid).orElse(StringUtils.EMPTY);
    }

    default MamsSession currentLoginSession(){
        MamsSession mamsSession = currentSession();
        if (Objects.isNull(mamsSession))
            Res.UserAuthFailure401Thr.thr();
        if (StringUtils.isBlank(mamsSession.getUserid()))
            Res.UserAuthFailure401Thr.thr();
        return mamsSession;
    }

    @Slf4j
    @Configuration
    @ConditionalOnMissingBean(MamsSessionAttribute.class)
    class NullMamsSessionAttribute implements MamsSessionAttribute{

        @Override
        public MamsSession currentSession() {
            log.warn("MamsSessionAttribute 未提供");
            return null;
        }

        @Override
        public int getOrder() {
            return Integer.MAX_VALUE;
        }
    }
}