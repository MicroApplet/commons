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

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 动态参数映射。
 *
 * <p>支持 Jackson 任意属性反序列化的参数容器，用于承载 HTTP 请求参数。
 * 通过 {@link JsonAnySetter} 和 {@link JsonAnyGetter} 实现未知属性的动态读写。
 * 序列化时，所有 {@code entries} 中的键值对将作为普通 JSON 属性输出。</p>
 *
 * <h3>序列化效果</h3>
 * <pre>{@code
 * ParamMap params = ParamMap.of("orderId", "123", "amount", "200");
 * // JSON: {"orderId": "123", "amount": "200"}
 * }</pre>
 */
@Data
@NoArgsConstructor
public class ParamMap {
    private final LinkedHashMap<String, Object> entries = new LinkedHashMap<>();

    /**
     * 添加参数。
     *
     * @param key   参数名
     * @param value 参数值
     * @return this
     */
    public ParamMap put(String key, Object value) {
        entries.put(key, value);
        return this;
    }

    /**
     * 获取所有参数
     *
     * @return 参数映射
     */
    public Map<String, Object> any() {
        return entries;
    }

    /**
     * 设置参数
     *
     * @param key   参数名
     * @param value 参数值
     */
    public void set(String key, Object value) {
        entries.put(key, value);
    }

    /**
     * 创建包含一个键值对的参数映射。
     *
     * @param k1 参数名
     * @param v1 参数值
     * @return ParamMap 实例
     */
    public static ParamMap of(String k1, Object v1) {
        return new ParamMap().put(k1, v1);
    }

    /**
     * 创建包含两个键值对的参数映射。
     *
     * @param k1 参数名1
     * @param v1 参数值1
     * @param k2 参数名2
     * @param v2 参数值2
     * @return ParamMap 实例
     */
    public static ParamMap of(String k1, Object v1, String k2, Object v2) {
        return new ParamMap().put(k1, v1).put(k2, v2);
    }
}
