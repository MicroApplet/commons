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

package com.asialjim.microapplet.commons.chl;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public final class PlatformTypeValue implements PlatformType, Serializable {
    public static final PlatformType UN_KNOWN = new PlatformTypeValue(unknown,unknown,unknown);
    public static final PlatformType UN_SUPPORT = new PlatformTypeValue(unSupport,unSupport,unSupport);
    @Serial
    private static final long serialVersionUID = -556676638261106759L;

    private String shortCode;
    private String code;
    private String name;
}