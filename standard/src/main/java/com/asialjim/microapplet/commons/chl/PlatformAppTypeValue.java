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
public final class PlatformAppTypeValue implements PlatformAppType , Serializable {
    public static final PlatformAppType UN_KNOW = new PlatformAppTypeValue(PlatformTypeValue.UN_KNOWN,PlatformType.unknown,PlatformType.unknown);
    public static final PlatformAppType UN_SUPPORT = new PlatformAppTypeValue(PlatformTypeValue.UN_SUPPORT,PlatformType.unSupport,PlatformType.unSupport);
    @Serial
    private static final long serialVersionUID = 7413856110592308709L;

    private PlatformType platformType;
    private String code;
    private String name;
}
