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

/**
 * 错误信息负载。
 *
 * <p>AI 处理过程中发生错误时，通过此负载将错误码和错误描述推送给前端。
 * 前端可据此展示错误提示，或根据错误码做相应处理。</p>
 *
 * <pre>{@code
 * SseEvent.error("AUTH_FAILED", "用户登录已过期，请重新登录")
 * }</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorPayload {
    /** 错误码，用于前端做程序化判断 */
    private String code;
    /** 错误描述，用于展示给用户 */
    private String message;
}
