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

package com.asialjim.microapplet.web.client;

import com.asialjim.microapplet.commons.cons.LogCons;

public interface MamsHttpHeaders {
    String HTTP_CLIENT_TYPE = "X-HTTP-Client-Type";
    String LOAD_BALANCE_CLIENT = "loadBalanceClient";
    String SESSION_ID = LogCons.SESSION;
    String TRACE_ID = LogCons.TRACE;
    String OPEN_ID = "x-open-id";

    String ENC_KEY_VERSION = "x-enc-key-version";

    String PLATFORM_TYPE = "x-platform-type";
    String APP_ID = "x-app-id";
    String APP_TYPE = "x-app-type";

    String USER_TOKEN_KEY = "x-user-token";

    String RES_STATUS = "x-res-status";
    String RES_SUCCESS = "x-res-success";
    String RES_CODE = "x-res-code";
    String RES_MSG = "x-res-msg";
    String RES_ERRS = "x-res-errs";
    String Authorization = "Authorization";
}