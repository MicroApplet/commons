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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标注字段需要数据库存储加密（确定性密文）。
 *
 * <p>与传输/日志脱敏的 {@code @Sensitive} 解耦：{@code @Sensitive} 负责网络传输与日志输出，
 * {@code @StoreEncrypt} 负责数据库存储。二者可同时标注在同一字段上。</p>
 *
 * <p>MyBatis 层适配时：</p>
 * <ul>
 *     <li>写入：对标注字段值用 {@link StoreCipher#encrypt(String)} 加密。</li>
 *     <li>读取：用 {@link StoreCipher#decrypt(String)} 解密。</li>
 *     <li>当 {@link #searchable()} 为 true 时，额外把明文 {@link StoreCipher#blindIndex(String)}
 *     的结果写入 {@link #blindIndexField()} 指定的盲索引字段。</li>
 * </ul>
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StoreEncrypt {

    /** 是否生成盲索引以支持模糊查询 */
    boolean searchable() default false;

    /**
     * 盲索引字段名（对应实体中另一个字段 / 数据库列）。
     * <p>留空时约定为当前字段名 + {@code Idx}，由适配层解析。</p>
     */
    String blindIndexField() default "Idx";
}
