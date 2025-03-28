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

package com.asialjim.microapplet.common.page;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * 是否启用分页
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class Pageable {

    public static <Page> Page ofPage(Supplier<PageParameter> pageParameterFun, Function<PageParameter, Page> pageFun) {
        if (Objects.isNull(pageParameterFun) || Objects.isNull(pageFun))
            return null;
        PageParameter pageParameter = pageParameterFun.get();
        if (Objects.isNull(pageParameter))
            return null;

        return pageFun.apply(pageParameter);
    }


    public static <Page> Page ofPage(Supplier<PageParameter> pageParameterFun, Function<PageParameter, Page> pageFun, Page defaultPage) {
        return Optional.ofNullable(ofPage(pageParameterFun, pageFun)).orElse(defaultPage);
    }

    public static <Page> Page ofPage(Supplier<PageParameter> pageParameterFun, Function<PageParameter, Page> pageFun, Supplier<Page> defaultPage) {
        return Optional.ofNullable(ofPage(pageParameterFun, pageFun)).orElseGet(defaultPage);
    }
}