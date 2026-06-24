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
import com.asialjim.microapplet.commons.standard.utils.JsonUtil;
import com.asialjim.microapplet.session.Session;
import com.asialjim.microapplet.session.Sessions;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 角色拦截器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/8/29, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Slf4j
@Aspect
@Component
public class RoleAdvice {

    @Before("@within(roleNeed)")
    public void checkClassRole(Role roleNeed) {
        checkRole(roleNeed);
    }

    @Before("@annotation(roleNeed)")
    public void checkMethodRole(Role roleNeed) {
        checkRole(roleNeed);
    }


    private void checkRole(Role roleNeed) {
        if (Objects.isNull(roleNeed)) return;

        Optional<Session> opt = Sessions.opt();

        String[] all = roleNeed.all();
        String[] any = roleNeed.any();

        if (ArrayUtils.isNotEmpty(all))
            all(all, opt);
        if (ArrayUtils.isNotEmpty(any))
            any(any, opt);
    }


    private void any(String[] roles, Optional<Session> opt) {
        final List<String> msg = check(roles, opt);
        if (CollectionUtils.isEmpty(msg))
            RoleRes.SessionNeedRolesAny.thr(msg);
    }


    private void all(String[] roles, Optional<Session> opt) {
        final List<String> msg = check(roles, opt);
        if (CollectionUtils.isEmpty(msg))
            RoleRes.SessionNeedRolesAll.thr(msg);
    }

    private @NonNull List<String> check(String[] roles, Optional<Session> opt) {
        final List<String> msg = new ArrayList<>();

        List<RoleChecker> checkers = RoleCheckerHub.candidate(roles);
        for (RoleChecker checker : checkers) {
            if (checker.sessionCheck(opt))
                continue;
            Map<String, String> map = Map.of("id", checker.getId(), "name", checker.getName());
            msg.add(JsonUtil.instance.toStr(map));
        }
        return msg;
    }
}