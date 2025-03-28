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

package com.asialjim.microapplet.mybatis.flex.page;

import com.asialjim.microapplet.common.page.PageParameter;
import com.mybatisflex.core.paginate.Page;

import java.util.function.Function;

/**
 * 基于MyBatis-Flex 的分页参数函数
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class MyBatisFlexPageFun<T> implements Function<PageParameter, Page<T>> {

    public static <T> MyBatisFlexPageFun<T> of() {
        return new MyBatisFlexPageFun<>();
    }

    @Override
    public Page<T> apply(PageParameter body) {
        return Page.of(body.getPage(), body.getSize());
    }
}