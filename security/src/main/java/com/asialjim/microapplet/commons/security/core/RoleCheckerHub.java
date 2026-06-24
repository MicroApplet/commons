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

package com.asialjim.microapplet.commons.security.core;


import com.asialjim.microapplet.commons.security.context.RoleRes;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Strings;

import java.util.*;
import java.util.function.Supplier;

public class RoleCheckerHub {
    private static final List<RoleChecker> list = new ArrayList<>();
    private static final Map<String, RoleChecker> map = new HashMap<>();
    private static final Set<String> ids = new HashSet<>();
    private static RoleCheckerHub instance;

    public RoleCheckerHub(Supplier<List<RoleChecker>> rolesProvider) {
        this(Optional.ofNullable(rolesProvider).map(Supplier::get).orElse(null));
    }

    public RoleCheckerHub(List<RoleChecker> roleCheckers) {
        if (CollectionUtils.isEmpty(roleCheckers))
            return;

        if (Objects.isNull(instance)) {
            synchronized (list) {
                if (Objects.isNull(instance))
                    instance = this;
            }
        }

        for (RoleChecker roleChecker : roleCheckers) {
            if (Objects.isNull(roleChecker))
                continue;
            String id = roleChecker.getId();
            if (!ids.contains(id))
                throw new IllegalStateException("存在重复的角色编号：" + id );

            ids.add(id);
            list.add(roleChecker);
        }
    }

    public static List<RoleChecker> candidate(String... tag){
        List<RoleChecker> res = new ArrayList<>();
        for (String s : tag) {
            if (StringUtils.isBlank(s))
                continue;
            RoleChecker roleChecker = tagOf(s);
            res.add(roleChecker);
        }
        return res;
    }

    public static RoleChecker tagOf(String tag) {
        return map.computeIfAbsent(tag, k -> list.stream()
                .filter(Objects::nonNull)
                .filter(item -> Strings.CI.equals(tag, item.getId()))
                .findFirst()
                .orElseThrow(() -> RoleRes.RoleNotExist.ex(tag)));
    }
}