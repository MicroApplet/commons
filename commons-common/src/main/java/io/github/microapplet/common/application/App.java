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

package io.github.microapplet.common.application;

import io.github.microapplet.common.event.EventUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 應用啓動器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/26, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class App {

    /**
     * 启动应用
     */
    public static void voidStart(Class<?> sourceClass, String[] args) {
        //noinspection unused
        AppStarted start = start(sourceClass, args);
    }

    /**
     * 启动应用
     */
    public static AppStarted start(Class<?> sourceClass, String[] args) {
        try {
            ConfigurableApplicationContext ctx = SpringApplication.run(sourceClass, args);
            return EventUtil.push(new AppStarted(ctx));
        } catch (Throwable t) {
            System.err.println("\r\n\tApplication Start Failure: " + t.getMessage());
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            throw t;
        }
    }
}