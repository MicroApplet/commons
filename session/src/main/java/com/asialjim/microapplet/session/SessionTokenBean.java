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

import com.asialjim.microapplet.commons.standard.utils.SessionTokenUtil;

/**
 * 会话令牌工具包装类。
 * <p>基于 {@link SessionTokenUtil} 实现令牌的创建和验证。</p>
 */
public final class SessionTokenBean {

    private SessionTokenBean() {}

    /**
     * 创建会话令牌。
     *
     * @return 令牌字符串
     */
    public static String create() {
        return SessionTokenUtil.create();
    }

    /**
     * 验证会话令牌是否有效。
     *
     * @param token 令牌
     * @return 有效返回 true
     */
    public static boolean verify(String token) {
        return SessionTokenUtil.verify(token);
    }
}
