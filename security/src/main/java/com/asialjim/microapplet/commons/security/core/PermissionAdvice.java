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

import com.asialjim.microapplet.commons.security.context.PermissionRes;
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
public class PermissionAdvice {

    @Before("@within(PermissionNeed)")
    public void checkClassPermission(Permission PermissionNeed) {
        checkPermission(PermissionNeed);
    }

    @Before("@annotation(PermissionNeed)")
    public void checkMethodPermission(Permission PermissionNeed) {
        checkPermission(PermissionNeed);
    }


    private void checkPermission(Permission PermissionNeed) {
        if (Objects.isNull(PermissionNeed)) return;

        Optional<Session> opt = Sessions.opt();

        String[] all = PermissionNeed.all();
        String[] any = PermissionNeed.any();

        if (ArrayUtils.isNotEmpty(all))
            all(all, opt);
        if (ArrayUtils.isNotEmpty(any))
            any(any, opt);
    }


    private void any(String[] Permissions, Optional<Session> opt) {
        final List<String> msg = check(Permissions, opt);
        if (CollectionUtils.isEmpty(msg))
            PermissionRes.SessionNeedPermissionAny.thr(msg);
    }


    private void all(String[] Permissions, Optional<Session> opt) {
        final List<String> msg = check(Permissions, opt);
        if (CollectionUtils.isEmpty(msg))
            PermissionRes.SessionNeedPermissionAll.thr(msg);
    }

    private @NonNull List<String> check(String[] Permissions, Optional<Session> opt) {
        final List<String> msg = new ArrayList<>();

        List<PermissionChecker> checkers = PermissionCheckerHub.candidate(Permissions);
        for (PermissionChecker checker : checkers) {
            if (checker.sessionCheck(opt))
                continue;
            Map<String, String> map = Map.of("id", checker.getId(), "name", checker.getName());
            msg.add(JsonUtil.instance.toStr(map));
        }
        return msg;
    }
}