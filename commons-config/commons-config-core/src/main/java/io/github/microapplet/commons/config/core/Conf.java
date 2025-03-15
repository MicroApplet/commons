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

package io.github.microapplet.commons.config.core;


import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * 顶级及接口：配置
 * <p/>
 * 标记类为配置信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface Conf<Config extends Conf<?>> {

    default Conf<Config> instance() {
        return null;
    }

    /**
     * 默认配置信息
     *
     * @return {@link Conf<Config> }
     * @since 2025/3/10
     */
    Conf<Config> deftConf();

    /**
     * 根据环境，获取配置信息
     *
     * @param env {@link Env env}
     * @return {@link Conf<Config> }
     * @since 2025/3/10
     */
    default Conf<Config> config(Env env) {
        Map<Env, Conf<Config>> configs = configs();
        if (Objects.isNull(configs) || configs.isEmpty())
            return deftConf();

        Conf<Config> configConf = configs.get(env);
        if (Objects.nonNull(configConf))
            return configConf;
        return deftConf();
    }

    /**
     * 多环境配置信息
     * <p/>
     * 允许在编写代码之初便写好配置信息
     * 但需要注意的是，不推荐将敏感信息写入代码中，此处仅限于对各个环境进行初始化配置（规定数据结构）
     */
    default Map<Env, Conf<Config>> configs() {
        return Collections.emptyMap();
    }

}