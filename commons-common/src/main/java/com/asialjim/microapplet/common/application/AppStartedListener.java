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

package com.asialjim.microapplet.common.application;

import com.asialjim.microapplet.common.event.Listener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 应用启动事件
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class AppStartedListener implements Listener<AppStarted> {
    private static final Logger log = LoggerFactory.getLogger(AppStartedListener.class);

    @Override
    public void doOnEvent(AppStarted event) {
        String name = event.ctx.getEnvironment().getProperty("spring.application.name");
        String format = String.format("\r\n\t---%s Started---", name);
        log.info(format);
    }
}