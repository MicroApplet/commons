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

import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 当前会话上下文
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class MamsSessionContextHolder {
    private static MamsSessionContextHolder instance;
    @Resource
    private List<MamsSessionAttribute> mamsSessionAttributes;

    @PostConstruct
    public void init() {
        instance = this;
    }

    public static Optional<MamsSession> current() {
        return Optional.ofNullable(instance)
                .map(item -> item.mamsSessionAttributes)
                .stream()
                .flatMap(Collection::stream)
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .map(MamsSessionAttribute::currentSession)
                .filter(Objects::nonNull)
                .findFirst();
    }
}