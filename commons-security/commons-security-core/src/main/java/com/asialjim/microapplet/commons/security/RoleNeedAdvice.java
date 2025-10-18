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
        long role = currentRoles.hasRole();
        if (role == 0)
            AuthorityRes.NoRole.thr();

        long[] all = roleNeed.all();
        long[] any = roleNeed.any();

        all(all, role);
        any(any, role);
    }


    private void any(long[] roles, long role) {
        final List<String> msg = new ArrayList<>();
        msg.add("至少需要满足以下任一角色[代码]");
        for (long l : roles) {
            if (RoleCode.contains(role, l))
                return;
            msg.add(String.valueOf(l));
        }

        AuthorityRes.NoRole.thr(msg);
    }


    private void all(long[] roles, long role) {
        final List<String> msg = new ArrayList<>();
        msg.add("还需要满足以下角色[代码]");
        boolean tag = false;
        for (long l : roles) {
            // 满足角色，下一个
            if (RoleCode.contains(role, l))
                continue;
            msg.add(String.valueOf(l));
            tag = true;
        }
        if (tag)
            AuthorityRes.NoRole.thr(msg);
    }
}