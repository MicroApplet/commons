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


import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.function.Function;

/**
 * 分页数据
 *
 * @author Asial Jim
 * @version 1.0
 * @since 2026/2/3, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface Pageable<T> {

    /**
     * 获取当前页码
     *
     * @return 当前页码
     * @since 2026-04-15
     */
    int getPage();

    /**
     * 获取每页大小
     *
     * @return 每页大小
     * @since 2026-04-15
     */
    int getSize();

    /**
     * 获取总页数
     *
     * @return 总页数
     * @since 2026-04-15
     */
    int getPages();

    /**
     * 获取总记录数
     *
     * @return 总记录数
     * @since 2026-04-15
     */
    int getTotal();

    /**
     * 获取记录列表
     *
     * @return {@link Collection}<{@link T}> 记录列表
     * @since 2026-04-15
     */
    Collection<T> getRecords();

    /**
     * 根据给定的集合、页码、页大小、总页数和总记录数创建一个分页对象
     *
     * @param collection 需要分页的集合
     * @param page       当前页码
     * @param size       每页大小
     * @param pages      总页数
     * @param total      总记录数
     * @param <T>        集合元素的泛型类型
     * @return 创建的分页对象
     */
    static <T> Page<T> paginate(Collection<T> collection, Number page,Number size, Number pages, Number total){
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);
        pages = Optional.ofNullable(pages).orElse(1);
        total = Optional.ofNullable(total).orElse(1);

        return new Page<>(page.intValue(), size.intValue(), pages.intValue(), total.intValue(), collection);
    }

    /**
     * 根据给定的集合、页码和页大小创建一个分页对象
     *
     * @param collection 需要分页的集合
     * @param page       当前页码
     * @param size       每页大小
     * @param <T>        集合元素的泛型类型
     * @return 创建的分页对象
     */
    static <T> Page<T> paginate(Collection<T> collection, Integer page, Integer size) {
        page = Optional.ofNullable(page).orElse(0);
        size = Optional.ofNullable(size).orElse(10);

        int total = collection.size();
        int fromIndex = page * size;
        int toIndex = Math.min(fromIndex + size, total);
        int pages = total / size + (total % size == 0 ? 0 : 1);

        Collection<T> res;
        if (fromIndex >= toIndex)
            res = Collections.emptyList();
        else
            res = new ArrayList<>(collection).subList(fromIndex, toIndex);

        return new Page<>(page, size, pages, total, res);
    }

    /**
     * 将一个分页对象中的数据转换为另一种类型的分页对象
     *
     * @param source 需要转换的分页对象
     * @param fun    用于转换数据的函数
     * @param <T>    源分页对象数据的泛型类型
     * @param <R>    目标分页对象数据的泛型类型
     * @return 转换后的分页对象
     */
    static <T, R> Page<R> parse(Pageable<T> source, Function<T, R> fun) {
        Collection<R> res;
        Collection<T> records = source.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            res = records.stream()
                    .map(fun)
                    .toList();
        } else {
            res = Collections.emptyList();
        }

        return new Page<>(
                source.getPage(), source.getSize(),
                source.getPages(), source.getTotal(),
                res);
    }
}
