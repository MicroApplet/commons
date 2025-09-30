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
import java.util.concurrent.atomic.AtomicLong;

/**
 * 角色拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Aspect
@Component
public class RoleNeedAdvice {
    @Resource
    private CurrentRoleBean currentRoleBean;


    @Before("@within(roleNeed)")
    public void checkClassRole(RoleNeed roleNeed) {
        checkRole(roleNeed);
    }

    @Before("@annotation(roleNeed)")
    public void checkMethodRole(RoleNeed roleNeed) {
        checkRole(roleNeed);
    }


    private void checkRole(RoleNeed roleNeed) {
        if (Objects.isNull(roleNeed)) return;
        CurrentRoles currentRoles = this.currentRoleBean.currentRole();
        List<Role> userHadRoles = currentRoles.hasRole();
        if (CollectionUtils.isEmpty(userHadRoles))
            AuthorityRes.NoRole.thr();

        AtomicLong atomicLong = new AtomicLong(0);
        userHadRoles.stream().map(Role::getBit).forEach(atomicLong::addAndGet);
        long role = atomicLong.get();

        Role[] all = roleNeed.all();
        if (ArrayUtils.isNotEmpty(all))
            all(all, role);
        Role[] any = roleNeed.any();
        if (ArrayUtils.isNotEmpty(any))
            any(any, role);
    }

    private void any(Role[] roles, long role) {
        Set<Role> set = new HashSet<>();
        for (Role role1 : roles) {
            boolean hasRole = role1.is(role);
            if (hasRole) return;
            set.add(role1);
        }

        List<String> list = set.stream().map(Role::getDesc).toList();
        AuthorityRes.NoRole.thr(new ArrayList<>(list));
    }


    private void all(Role[] roles, long role) {
        Set<Role> set = new HashSet<>();
        for (Role role1 : roles) {
            boolean hasRole = role1.is(role);
            if (!hasRole) set.add(role1);
        }
        if (CollectionUtils.isEmpty(set)) return;

        List<String> list = set.stream().map(Role::getDesc).toList();
        AuthorityRes.NoRole.thr(new ArrayList<>(list));
    }
}