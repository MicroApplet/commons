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

package com.asialjim.microapplet.common.cons;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 请求头
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/23, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface Headers {
    String SessionId = "x-session-id";
    String TraceId = "x-trace-id";
    String CloudAgent = "x-cloud-agent";

    String HTTPLogLevel = "x-app-log-level";

    String CURRENT_SESSION = "x-current-session";
    String AUTH_PARAMETERS_HEADER = "x-app-auth-parameters";

    String APPLICATION_JSON = "application/json";
    String CONTENT_TYPE = "content-type";
    String CLIENT_TYPE = "client-type";
    String CLOUD_CLIENT = "cloud-client";

    String AUTHORIZATION = "authorization";

    String APP_ID = "x-app-id";
    String APP_CHL = "x-app-chl";
    String APP_CHL_APPID = "x-app-chl-appid";
    String APP_CHL_APP_TYPE = "x-app-chl-app-type";
    String APP_CHL_ENV = "x-app-chl-env";
    String APP_VERSION = "x-app-version";
    String APP_USER_NAME = "x-app-user-name";
    String APP_USER_PASSWORD = "x-app-user-password";
    String USER_TOKEN = "x-user-token";
    String TOKEN = "token";

    String TRACE_ID = "trace-id";

    String X_RES_THROWABLE = "x-res-throwable";
    String X_RES_CODE = "x-res-code";
    String X_RES_MSG = "x-res-msg";
    String X_RES_ERRS = "x-res-errs";
    String X_RES_STATUS = "x-res-status";
    String X_RES_PAGE = "x-res-page";
    String X_RES_SIZE = "x-res-size";
    String X_RES_PAGES = "x-res-pages";
    String X_RES_TOTAL = "x-res-total";

    List<String> headers = Stream.of(
            SessionId,
            TraceId,
            CloudAgent,
            HTTPLogLevel,
            CURRENT_SESSION,
            AUTH_PARAMETERS_HEADER,
            APPLICATION_JSON,
            CONTENT_TYPE,
            CLIENT_TYPE,
            CLOUD_CLIENT,
            AUTHORIZATION,
            APP_ID,
            APP_CHL,
            APP_CHL_APPID,
            APP_CHL_ENV,
            APP_VERSION,
            APP_USER_NAME,
            APP_USER_PASSWORD,
            USER_TOKEN,
            TRACE_ID,
            X_RES_THROWABLE,
            X_RES_CODE, X_RES_MSG, X_RES_ERRS, X_RES_STATUS, X_RES_PAGE, X_RES_SIZE, X_RES_PAGES, X_RES_TOTAL).collect(Collectors.toList());

    List<String> TRACE_HEADERS = Stream.of("traceid", TRACE_ID, "trace_id", "request_id").collect(Collectors.toList());
}