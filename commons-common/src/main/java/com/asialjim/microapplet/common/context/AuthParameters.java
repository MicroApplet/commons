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

package com.asialjim.microapplet.common.context;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;

import static com.asialjim.microapplet.common.cons.WebCons.BEARER_PREFIX;


/**
 * 登录参数
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/9/18, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Data
@Accessors(chain = true)
public class AuthParameters implements Serializable {
    @Serial
    private static final long serialVersionUID = -2870486120531650447L;

    private String authorization;
    private String token;
    private String ticket;
    private String accessToken;
    private String code;
    private String state;

    private String chl;
    private String chlAppid;
    private String chlEnv;
    private String version;

    private String username;
    private String password;

    public void setAuthorization(String authorization) {
        if (StringUtils.isNotBlank(authorization))
            this.authorization = authorization.replaceFirst(BEARER_PREFIX, StringUtils.EMPTY);
    }
}