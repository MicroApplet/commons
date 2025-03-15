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

package io.github.microapplet.web.mvc.config;

/**
 * 配置：部分URL资源跳过将响应结果包装为 Result<?>结构体
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface SkipParseRes2ResultConfig {

    /**
     * 判定是否需要跳过转换
     *
     * @param url {@link String url}
     * @return {@link boolean }
     * @since 2025/3/14
     */
    boolean skipParseRes2ResultCheck(String url, String context);
}