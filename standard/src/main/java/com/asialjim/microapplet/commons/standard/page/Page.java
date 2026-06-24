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

package com.asialjim.microapplet.commons.standard.page;

import java.util.Collection;

/**
 * 默认的分页数据包
 *
 * @author Asial Jim
 * @version 1.0
 * @since 2026/2/6, &nbsp;&nbsp; <em>version:1.0</em>
 */
public record Page<T>(
        /*
          当前页码
         */
        int page,
        /*
          每页大小
         */
        int size,
        /*
          总页数
         */
        int pages,
        /*
          总记录数
         */
        int total,
        /*
          记录列表
         */
        Collection<T> records
) implements Pageable<T> {

    /**
     * 获取当前页码
     *
     * @return 当前页码
     * @since 2026-04-15
     */
    @Override
    public int getPage() {
        return page;
    }

    /**
     * 获取每页大小
     *
     * @return 每页大小
     * @since 2026-04-15
     */
    @Override
    public int getSize() {
        return size;
    }

    /**
     * 获取总页数
     *
     * @return 总页数
     * @since 2026-04-15
     */
    @Override
    public int getPages() {
        return pages;
    }

    /**
     * 获取总记录数
     *
     * @return 总记录数
     * @since 2026-04-15
     */
    @Override
    public int getTotal() {
        return total;
    }

    /**
     * 获取记录列表
     *
     * @return {@link Collection}<{@link T}> 记录列表
     * @since 2026-04-15
     */
    @Override
    public Collection<T> getRecords() {
        return records;
    }
}
