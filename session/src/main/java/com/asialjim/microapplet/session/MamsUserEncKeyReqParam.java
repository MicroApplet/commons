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
import org.apache.commons.lang3.math.NumberUtils;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户加密密钥参数请求
 */
@Data
public class MamsUserEncKeyReqParam implements Serializable {
    @Serial
    private static final long serialVersionUID = 2675695624928092046L;

    private String appid;
    private String openid;
    private Long version;

    public void setVersion(String version){
        this.version = NumberUtils.toLong(version,0);
    }

    public void setVersion(Long version){
        this.version = version;
    }
}