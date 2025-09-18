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

package com.asialjim.microapplet.commons.security;

import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 角色拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Aspect
@Component
public class PermissionNeedAdvice {
    @Resource
    private CurrentPermissionBean currentPermissionBean;

    @Before(value = "(@within(classPermissionNeed) || @annotation(methodPermissionNeed) )", argNames = "classPermissionNeed,methodPermissionNeed")
    public void before(PermissionNeed classPermissionNeed, PermissionNeed methodPermissionNeed) {
        checkRole(classPermissionNeed);
        checkRole(methodPermissionNeed);
    }

    private void checkRole(PermissionNeed permissionNeed) {
        if (Objects.isNull(permissionNeed)) return;

        CurrentPermission currentPermission = this.currentPermissionBean.currentPermission();
        List<Permission> userHadPermissions = currentPermission.hasPermission();
        if (CollectionUtils.isEmpty(userHadPermissions))
            AuthorityRes.NoPermission.thr();

        List<String> userHad = userHadPermissions.stream().map(Permission::getCode).toList();

        String[] all = permissionNeed.all();
        String[] any = permissionNeed.any();

        if (ArrayUtils.isNotEmpty(all))
            all(all, userHad);
        if (ArrayUtils.isNotEmpty(any))
            any(any, userHad);
    }

    private void any(String[] anyPermissions, List<String> userHadPermissions) {
        List<Object> missPermissions = new ArrayList<>();
        for (String permission : anyPermissions) {
            boolean userHadPermission = userHadPermissions.contains(permission);
            if (userHadPermission)
                return;
            missPermissions.add(permission);
        }
        AuthorityRes.NoPermission.thr(missPermissions);
    }


    private void all(String[] allPermissions, List<String> userHadPermissions) {
        List<Object> missPermissions = new ArrayList<>();
        for (String permission : allPermissions) {
            // 用户有权限
            if (userHadPermissions.contains(permission))
                continue;
            missPermissions.add(permission);
        }

        if (CollectionUtils.isEmpty(missPermissions))
            return;

        AuthorityRes.NoPermission.thr(missPermissions);
    }
}