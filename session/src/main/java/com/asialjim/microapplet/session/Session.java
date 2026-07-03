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

import com.asialjim.microapplet.commons.chl.PlatformAppType;
import lombok.Data;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

@Data
public class Session implements Serializable {
    @Serial
    private static final long serialVersionUID = -4560156189056466532L;

    private String id;
    private String trace;
    private String token;
    private String csrf;
    private String userid;
    private String openid;
    private String unionid;
    private String nickname;
    private String platType;
    private String appid;
    private String appType;
    private String sessionKey;
    private String userCode;
    private String userToken;
    private LocalDateTime loginTime;
    private LocalDateTime expireAt;
    private LocalDateTime lastLoginTime;

    public static Session tourist(){
        Session session = new Session();
        session.setId("TOURIST-" + UUID.randomUUID().toString().replace("-",StringUtils.EMPTY));
        session.setUserid(session.getId());
        session.setOpenid(session.getId());
        session.setUnionid(session.getId());
        session.setNickname("游客");
        session.setPlatType("TOURIST");
        session.setAppid("TOURIST");
        session.setAppType("TOURIST");
        return session;
    }


    public Session expireAfter(Duration duration) {
        this.expireAt = LocalDateTime.now().plusMinutes(duration.toMinutes());
        return this;
    }

    public boolean isExpired() {
        LocalDateTime expireAt = getExpireAt();
        if (Objects.isNull(expireAt))
            return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(expireAt);
    }

    public PlatformAppType platformAppType() {
        return PlatformAppType.idOf(getPlatType() + ":" + getAppType());
    }

    public String clientIp() {
        return StringUtils.EMPTY;
    }

    public String platformTypeCode() {
        return Optional.ofNullable(platformAppType()).map(item -> item.getPlatformType()).map(item -> item.getCode()).orElse(StringUtils.EMPTY);
    }
}