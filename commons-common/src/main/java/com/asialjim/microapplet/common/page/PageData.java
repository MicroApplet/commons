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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collection;
import java.util.Objects;

/**
 * 分页数据
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
public class PageData<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 5125243427769117213L;

    /**
     * 当前页码
     */
    private Long page;
    /**
     * 总页码
     */
    private Long pages;
    /**
     * 页宽度
     */
    private Long size;
    /**
     * 当前页记录条数
     */
    private Long recordSize;
    /**
     * 总记录条数
     */
    private Long total;
    /**
     * 是否有下一页
     */
    private Boolean hasNext;
    /**
     * 当前页记录
     */
    private Collection<T> records;

    @SuppressWarnings("unused")
    public PageData(Collection<T> records) {
        this.records = records;
        this.page = 1L;
        this.pages = 1L;
        this.size = Objects.nonNull(records) ? records.size() : 0L;
        this.recordSize = this.size;
        this.total = this.size;
        this.hasNext = false;
    }

    @SuppressWarnings("unused")
    public Boolean getHasNext() {
        return this.getPage() < this.getPages();
    }

    @SuppressWarnings("unused")
    public Long getRecordSize() {
        return Objects.nonNull(records) ? records.size() : 0L;
    }

    public Long getPages() {
        if (Objects.nonNull(this.pages))
            return this.pages;
        Long total = this.getTotal();
        Long size = this.getSize();
        if (Objects.isNull(total) || Objects.isNull(size) || size == 0L)
            return 0L;

        return (total / size) + (total % size > 0 ? 1 : 0);
    }
}