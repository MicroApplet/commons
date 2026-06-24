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

package com.asialjim.microapplet.sensitive.logback;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.core.spi.ContextAwareBase;

/**
 * Logback SPI 自动配置。
 * <p>当 classpath 中存在 {@code logback-classic} 时，Logback 通过 ServiceLoader
 * 自动发现此配置器，将 {@link SensitiveMessageConverter} 注册为 {@code %msg} 的默认转换器。
 * 设置 {@link ExecutionStatus#NEUTRAL}，不覆盖应用已有的 Logback 配置。</p>
 *
 * <p>引入此模块后，开发者无需任何配置，以下代码自动脱敏：</p>
 * <pre>{@code
 * log.info("user: {}", user);
 * // 输出: user: {"name":"张**","phone":"138****1234"}
 * }</pre>
 *
 * @see SensitiveMessageConverter
 */
public class SensitiveLogbackConfigurator extends ContextAwareBase implements Configurator {

    @Override
    public ExecutionStatus configure(LoggerContext lc) {
        addInfo("注册敏感数据日志脱敏转换器");
        PatternLayout.DEFAULT_CONVERTER_SUPPLIER_MAP.put(
                "msg",
                SensitiveMessageConverter::new
        );
        return ExecutionStatus.NEUTRAL;
    }
}
