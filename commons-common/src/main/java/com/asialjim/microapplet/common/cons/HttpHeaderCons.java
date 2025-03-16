/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.common.cons;

/**
 * HTTP 请求头
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface HttpHeaderCons {

    String HTTP_LOG_LEVEL = "X-App-Log-Level";
    String APPID = "X-App-Appid";
    String CHL_CODE = "X-App-Chl-Code";
    String CHL_APPID = "X-App-Chl-Appid";
    String CHL_APP_TYPE = "X-App-Chl-App-Type";
    String Authorization = "Authorization";

}