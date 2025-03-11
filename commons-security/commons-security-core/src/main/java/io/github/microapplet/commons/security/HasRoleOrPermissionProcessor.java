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

package io.github.microapplet.commons.security;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 登录用户持有角色处理器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Aspect
@Component
public class HasRoleOrPermissionProcessor {
    @Resource
    private CurrentPermissionBean currentPermissionBean;
    @Resource
    private CurrentRoleBean currentRoleBean;

    @Before("@annotation(HasRole)")
    public void beforeHasRole(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HasRole annotation = method.getAnnotation(HasRole.class);
        //noinspection DuplicatedCode
        Set<Long> roleNeededSet = new HashSet<>();
        for (long l : annotation.value()) {
            // 游客就可以访问
            if (l == Tourist.code)
                return;

            roleNeededSet.add(l);
        }

        CurrentRoles currentRoles = this.currentRoleBean.currentRole();
        List<Long> roleCodes = currentRoles.roleCodes();

        boolean allMatch = new HashSet<>(roleCodes).containsAll(roleNeededSet);
        if (allMatch)
            return;

        AuthorityRes.NoRole.throwBiz();
    }

    @Before("@annotation(HasPermission)")
    public void beforeHasPermission(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        HasPermission annotation = method.getAnnotation(HasPermission.class);
        CurrentPermission currentPermission = this.currentPermissionBean.currentPermission();
        List<Long> permissionCodeHas = currentPermission.permissionCodes();
        //noinspection DuplicatedCode
        Set<Long> permissionNeededSet = new HashSet<>();
        for (long l : annotation.value()) {
            permissionNeededSet.add(l);
        }
        boolean allMatch = new HashSet<>(permissionCodeHas).containsAll(permissionNeededSet);
        if (allMatch)
            return;

        AuthorityRes.NoPermission.throwBiz();
    }
}