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
 * 操作按钮类型枚举。
 *
 * <p>定义 UI 卡片操作按钮的交互行为类型，决定用户点击按钮后的处理方式。</p>
 *
 * <table border="1">
 *   <tr><th>类型</th><th>说明</th><th>行为</th></tr>
 *   <tr><td>{@link #API}</td><td>直调后端</td><td>向 {@code url} 发送请求，不绕 AI</td></tr>
 *   <tr><td>{@link #LINK}</td><td>跳转链接</td><td>打开 {@code url} 指定的链接</td></tr>
 *   <tr><td>{@link #SUBMIT}</td><td>表单提交</td><td>收集表单数据后提交</td></tr>
 *   <tr><td>{@link #CALLBACK}</td><td>回调 AI</td><td>执行结果回传给 AI 继续处理</td></tr>
 * </table>
 *
 * @see Action
 */
public enum ActionType {

    /** 直调后端 API，不绕 AI，适用于纯数据查询和操作 */
    API("api"),
    /** 打开外部链接 */
    LINK("link"),
    /** 表单数据提交 */
    SUBMIT("submit"),
    /** 执行结果回传 AI 继续处理，适用于需要多轮交互的场景 */
    CALLBACK("callback");

    private final String name;

    ActionType(String name) { this.name = name; }

    public String getName() { return name; }

    public static ActionType fromName(String name) {
        for (ActionType t : values()) {
            if (t.name.equals(name)) return t;
        }
        return API;
    }
}
