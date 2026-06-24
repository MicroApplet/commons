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

package com.asialjim.microapplet.ai.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 结构化数据负载。
 *
 * <p>用于承载结构化的数据集合，如表格数据、列表数据等。
 * {@code schema} 描述数据结构定义，{@code data} 为具体数据行，
 * {@code summary} 为数据摘要文本。</p>
 *
 * @param <T> 数据行类型
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPayload<T> {
    /** 数据结构描述（可选，前端可用于动态生成表头） */
    private Object schema;
    /** 数据行列表 */
    private List<T> data;
    /** 数据摘要文本 */
    private String summary;

    public DataPayload(Object schema, List<T> data) {
        this.schema = schema;
        this.data = data;
    }
}
