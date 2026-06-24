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

package com.asialjim.microapplet.session;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户登录请求参数
 *
 * @author Asial Jim
 * @version 1.0
 * @since 2026/2/9, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
public class LoginReqVo implements Serializable {

    @Serial
    private static final long serialVersionUID = -8794853847294837513L;

    private String code;
    private String anonymousCode;
}