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
import lombok.Data;

import java.io.Serializable;

/**
 * 分页参数
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/28, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@AllArgsConstructor
public class PageParameter implements Serializable {
    private static final long serialVersionUID = -5786924401647566489L;
    public static final String pageName = "page";
    public static final String sizeName = "size";

    private final long page;
    private final long size;

    public static PageParameter pageOf() {
        return new PageParameter(1,10);
    }

    public static PageParameter pageOf(long page, long size) {
        return new PageParameter(page,size);
    }
}