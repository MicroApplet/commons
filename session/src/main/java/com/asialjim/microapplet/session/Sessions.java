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

package com.asialjim.microapplet.session;

import com.asialjim.microapplet.commons.standard.context.Res;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.Ordered;
import reactor.core.publisher.Mono;

import java.util.*;

@AllArgsConstructor
public class Sessions {
    private static Sessions instance;
    private final List<SessionCtx> sessions;

    public void init() {
        instance = this;
    }

    public static Optional<Session> opt() {
        if (Objects.isNull(instance))
            return Optional.empty();
        List<SessionCtx> sessions = instance.sessions;

        if (Objects.isNull(sessions))
            return Optional.empty();

        if (sessions.isEmpty())
            return Optional.empty();

        return sessions
                .stream()
                .sorted(Comparator.comparingInt(Ordered::getOrder))
                .map(SessionCtx::currentSession)
                .filter(Objects::nonNull)
                .findFirst();
    }

    public static Mono<Session> mono() {
        return opt().map(Mono::just).orElseGet(Mono::empty);
    }

    public static Optional<String> userid() {
        return opt().map(Session::getUserid);
    }

    public static Mono<String> useridMono() {
        return mono().map(Session::getUserid);
    }

    public static Session loginSession(){
        if (opt().isEmpty())
            Res.UserAuthFailure401Thr.thr();
        Session session = opt().get();
        String userid = session.getUserid();
        if (StringUtils.isBlank(userid))
            Res.UserAuthFailure401Thr.thr();

        return session;
    }

    public static Mono<Session> loginSessionMono() {
        return Mono.fromCallable(Sessions::loginSession);
    }
}