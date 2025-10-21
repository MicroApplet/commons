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

package com.asialjim.microapplet.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface Listener<E> {
    Logger log = LoggerFactory.getLogger(Listener.class);

    default void onEvent(E event) {
        try {
            log.info("监听事件开始：{}", event);
            doOnEvent(event);
            log.info("监听事件结束：{}", event);
        } catch (Throwable e) {
            if (log.isDebugEnabled())
                log.error("监听事件：{},异常:{}", event, e.getMessage(), e);
            else
                log.info("监听事件：{},异常:{}", event, e.getMessage());
            whenException(event, e);
        } finally {
            finallyFunction(event);
        }
    }

    void doOnEvent(E event) throws Throwable;

    default void whenException(E event, Throwable ex) {
        // do nothing here default
    }

    default void finallyFunction(E event) {
        // do nothing here default
    }
}