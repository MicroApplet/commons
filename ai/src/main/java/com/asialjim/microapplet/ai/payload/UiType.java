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

/**
 * UI 卡片类型枚举。
 *
 * <p>定义 AI 动态 UI 的渲染类型，每种类型对应前端 SDK 内置的一种渲染策略。</p>
 *
 * <h3>基础类型（前端 SDK 内置渲染器，数据驱动，无需定制）</h3>
 * <table border="1">
 *   <tr><th>类型</th><th>说明</th><th>渲染方式</th></tr>
 *   <tr><td>{@link #CARD}</td><td>信息展示卡片</td><td>标题 + props 遍历 + actions 按钮</td></tr>
 *   <tr><td>{@link #LIST}</td><td>列表</td><td>可点击列表项</td></tr>
 *   <tr><td>{@link #TABLE}</td><td>表格</td><td>schema 做表头 + data 做行</td></tr>
 *   <tr><td>{@link #FORM}</td><td>表单</td><td>输入字段 + 提交按钮</td></tr>
 *   <tr><td>{@link #CONFIRM}</td><td>确认对话框</td><td>提示 + 确认/取消按钮</td></tr>
 * </table>
 *
 * @see UiPayload
 */
public enum UiType {

    /** 信息展示卡片：标题 + 属性列表 + 操作按钮 */
    CARD("card"),
    /** 列表：可点击的列表项 */
    LIST("list"),
    /** 表格：schema 定义列 + data 填充行 */
    TABLE("table"),
    /** 表单：输入字段 + 提交动作 */
    FORM("form"),
    /** 确认对话框：提示文本 + 确认/取消按钮 */
    CONFIRM("confirm");

    private final String name;

    UiType(String name) { this.name = name; }

    public String getName() { return name; }

    public static UiType fromName(String name) {
        for (UiType t : values()) {
            if (t.name.equals(name)) return t;
        }
        return CARD;
    }
}
