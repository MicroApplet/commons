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

import com.asialjim.microapplet.commons.standard.context.Res;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public sealed interface PlatformAppType permits SupportPlatformAppType, PlatformAppTypeValue {
    List<PlatformAppType> all = new ArrayList<>();
    Map<String, PlatformAppType> map = new ConcurrentHashMap<>();

    static List<PlatformAppType> all() {
        if (CollectionUtils.isEmpty(all)) {
            synchronized (all) {
                if (CollectionUtils.isEmpty(all)) {
                    all.addAll(Arrays.asList(SupportPlatformAppType.values()));
                }
            }
        }
        return all;
    }

    static PlatformAppType idOf(String id) {
        if (StringUtils.isBlank(id))
            return PlatformAppTypeValue.UN_KNOW;

        return map.computeIfAbsent(id, k -> all().stream()
                .filter(Objects::nonNull)
                .filter(item -> Strings.CI.equalsAny(k, item.uniCode()))
                .findFirst()
                .orElse(PlatformAppTypeValue.UN_SUPPORT));
    }

    static boolean isUnknown(PlatformAppType type){
        if (Objects.isNull(type))
            return true;
        String code = type.getCode();
        return PlatformType.unknown.equalsIgnoreCase(code);
    }
    static boolean isUnSupport(PlatformAppType type){
        if (Objects.isNull(type))
            return true;
        String code = type.getCode();
        return PlatformType.unSupport.equalsIgnoreCase(code);
    }

    static PlatformAppType check(PlatformAppType type) {
        if (isUnknown(type))
            Res.UnKnownPlatformType.thr();
        if (isUnSupport(type))
            Res.UnSupportPlatformType.thr();
        return type;
    }

    static PlatformAppType codeOf(String platformType, String appType) {
        return idOf(platformType + ":" + appType);
    }

    PlatformType getPlatformType();

    String getCode();

    String getName();

    default String uniCode() {
        return getPlatformType().getCode() + ":" + getCode();
    }

    default PlatformAppType dto(){
        return new PlatformAppTypeValue(getPlatformType().dto(), getCode(), getName());
    }

    default boolean sameWith(PlatformAppType appType){
        if (Objects.isNull(appType))
            return false;
        return this.uniCode().equals(appType.uniCode());
    }

}