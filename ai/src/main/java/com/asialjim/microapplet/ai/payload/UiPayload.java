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

import java.util.List;

/**
 * 动态 UI 卡片。
 *
 * <p>AI 生成的交互式界面组件，用于向用户展示结构化信息和操作入口。
 * 前端的 UI 渲染器根据 {@code uiType} 选择对应的渲染策略，
 * 将 {@code props} 渲染为属性展示区，{@code actions} 渲染为操作按钮。</p>
 *
 * <h3>渲染策略</h3>
 * <ul>
 *   <li><b>基础组件</b> — 前端 SDK 内置通用渲染器，数据驱动，无需定制开发
 *     <ul>
 *       <li>{@link UiType#CARD CARD} — 信息卡片（标题 + 属性列表 + 操作按钮）</li>
 *       <li>{@link UiType#LIST LIST} — 可点击列表</li>
 *       <li>{@link UiType#TABLE TABLE} — 表格（schema + data）</li>
 *       <li>{@link UiType#FORM FORM} — 输入表单</li>
 *       <li>{@link UiType#CONFIRM CONFIRM} — 确认对话框</li>
 *     </ul>
 *   </li>
 *   <li><b>复杂组件</b> — 前端通过 {@code registerUiType(type, renderer)} 注册自定义渲染器
 *     <ul>
 *       <li>如 {@code chart}、{@code map}、{@code timeline} 等</li>
 *     </ul>
 *   </li>
 * </ul>
 *
 * <h3>按钮交互</h3>
 * <p>用户点击操作按钮时，前端触发 {@code action} 事件，由页面层根据
 * ActionType 处理：</p>
 * <ul>
 *   <li>{@link ActionType#API API} — 直调后端 {@code /api/direct/*}，不绕 AI</li>
 *   <li>{@link ActionType#LINK LINK} — 跳转到指定链接</li>
 *   <li>{@link ActionType#SUBMIT SUBMIT} — 提交表单数据</li>
 *   <li>{@link ActionType#CALLBACK CALLBACK} — 结果回传给 AI 继续处理</li>
 * </ul>
 *
 * <pre>{@code
 * UiPayload card = new UiPayload();
 * card.setUiType(UiType.CARD);
 * card.setTitle("订单 #ORD12345");
 * card.setProps(List.of(
 *     Prop.of("amount", "金额", "¥200"),
 *     Prop.of("status", "状态", "已支付")
 * ));
 * card.setActions(List.of(
 *     Action.of("退款", ActionType.API, "POST", "/api/direct/refund", ParamMap.of("orderId", "ORD12345"))
 * ));
 * SseEvent.ui(card).toSseFrame()
 * }</pre>
 *
 * @see UiType
 * @see ActionType
 * @see Prop
 * @see Action
 */
@Data
public class UiPayload {
    /** UI 卡片类型，决定前端的渲染策略 */
    private UiType uiType;
    /** 卡片标题 */
    private String title;
    /** 属性列表，展示为键值对 */
    private List<Prop> props;
    /** 操作按钮列表 */
    private List<Action> actions;
}
