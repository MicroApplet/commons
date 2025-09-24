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
    String SessionId = "X-Session-Id";
    String TraceId = "X-Trace-Id";
    String CloudAgent = "X-Cloud-Agent";

    String HTTPLogLevel = "X-App-Log-Level";

    String CURRENT_SESSION = "X-Current-Session";
    String AUTH_PARAMETERS_HEADER = "X-App-Auth-Parameters";

    String APPLICATION_JSON = "application/json";
    String CONTENT_TYPE = "Content-Type";
    String CLIENT_TYPE = "Client-Type";
    String CLOUD_CLIENT = "Cloud-Client";

    String AUTHORIZATION = "Authorization";

    String APP_ID = "X-App-ID";
    String APP_CHL = "X-App-Chl";
    String APP_CHL_APPID = "X-App-Chl-Appid";
    String APP_CHL_ENV = "X-App-Chl-Env";
    String APP_VERSION = "X-App-Version";
    String APP_USER_NAME = "X-App-User-Name";
    String APP_USER_PASSWORD = "X-App-User-Password";
    String USER_TOKEN = "X-User-Token";
    String TOKEN = "token";

    String TRACE_ID = "trace-id";

    String X_RES_THROWABLE = "X-Res-Throwable";
    String X_RES_CODE = "X-Res-Code";
    String X_RES_MSG = "X-Res-Msg";
    String X_RES_ERRS = "X-Res-Errs";
    String X_RES_STATUS = "X-Res-Status";
    String X_RES_PAGE = "X-Res-Page";
    String X_RES_SIZE = "X-Res-Size";
    String X_RES_PAGES = "X-Res-Pages";
    String X_RES_TOTAL = "X-Res-Total";

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

    List<String> TRACE_HEADERS = Stream.of("traceid", TRACE_ID, "trace_id", "request_id", "REQUEST_ID").collect(Collectors.toList());
}