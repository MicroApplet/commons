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

package com.asialjim.microapplet.sensitive.mybatis.enc;

/**
 * 盲索引切分粒度。
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
public enum BlindIndexGranularity {
    /** 单字符：召回最全，索引最大，假阳性多 */
    UNIGRAM(1),
    /** 二元组：支持任意子串模糊，索引适中（推荐） */
    BIGRAM(2),
    /** 三元组：索引更小，但短于 3 字符无法匹配 */
    TRIGRAM(3);

    private final int n;

    BlindIndexGranularity(int n) {
        this.n = n;
    }

    public int n() {
        return n;
    }
}
