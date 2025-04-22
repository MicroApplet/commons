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

package com.asialjim.microapplet.commons.security.web;

import com.asialjim.microapplet.common.exception.UnLoginException;
import com.asialjim.microapplet.commons.security.SecurityManager;

import javax.servlet.http.HttpServletRequest;

/**
 * 会话安全管理器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface SessionSecurityManager extends SecurityManager {

    default boolean skip(HttpServletRequest request) {
        return false;
    }

    default void loginCheck(HttpServletRequest request) {
        boolean skip = skip(request);
        if (skip)
            return;
        doLoginCheck(request);
    }

    /**
     * 登录检查
     */
    void doLoginCheck(HttpServletRequest request) throws UnLoginException;
}