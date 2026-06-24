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

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.*;

public sealed interface PlatformType permits SupportPlatformType, PlatformTypeValue {
    String unknown = "unknown";
    String unSupport = "unSupport";

    List<PlatformType> all = new ArrayList<>();
    Map<String,PlatformType> map = new HashMap<>();

    static List<PlatformType> all(){
        if (CollectionUtils.isEmpty(all)){
            synchronized (all){
                if (CollectionUtils.isEmpty(all)){
                    all.addAll(Arrays.asList(SupportPlatformType.values()));
                }
            }
        }
        return all;
    }

    static PlatformType of(String tag) {
        if (StringUtils.isBlank(tag))
            return PlatformTypeValue.UN_KNOWN;

        return map.computeIfAbsent(tag, k -> all().stream()
                .filter(Objects::nonNull)
                .filter(item -> Strings.CI.equalsAny(tag, item.getCode(), item.getName(), item.getShortCode()))
                .findFirst()
                .orElse(PlatformTypeValue.UN_SUPPORT));
    }

    String getShortCode();

    String getCode();

    String getName();

    default PlatformType dto(){
        return new PlatformTypeValue(getShortCode(),getCode(),getName());
    }
}