/*
 *    Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.asialjim.microapplet.web.mvc.security;

import com.asialjim.microapplet.common.cons.Headers;
import com.asialjim.microapplet.common.security.MamsSession;
import com.asialjim.microapplet.common.security.MamsSessionAttribute;
import com.asialjim.microapplet.common.utils.JsonUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;

/**
 * 基于mvc的当前登录用户会话属性
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/10/13, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
public class MVCMamsSessionAttribute implements MamsSessionAttribute {
    @Override
    public MamsSession currentSession() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) requestAttributes;
        if (Objects.isNull(servletRequestAttributes))
            return new MamsSession();

        HttpServletRequest request = servletRequestAttributes.getRequest();
        //noinspection ConstantValue
        if (Objects.isNull(request))
            return new MamsSession();

        String sessionJson = request.getHeader(Headers.CURRENT_SESSION);
        if (StringUtils.isNotBlank(sessionJson))
            return JsonUtil.instance.toBean(sessionJson, MamsSession.class);

        return new MamsSession();
    }

    @Override
    public int getOrder() {
        return Integer.MIN_VALUE;
    }
}