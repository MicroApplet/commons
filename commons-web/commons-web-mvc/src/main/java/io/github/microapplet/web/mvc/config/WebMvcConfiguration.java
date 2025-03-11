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

package io.github.microapplet.web.mvc.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Objects;

/**
 * 拦截器注册
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private List<HandlerInterceptor> handlerInterceptors;

    @Autowired(required = false)
    public void setHandlerInterceptors(List<HandlerInterceptor> handlerInterceptors) {
        this.handlerInterceptors = handlerInterceptors;
    }

    /**
     * 需要自行处理是否需要拦截
     *
     * @param registry {@link InterceptorRegistry registry}
     * @since 2025/3/11
     */
    @Override
    public void addInterceptors(@SuppressWarnings("NullableProblems") InterceptorRegistry registry) {
        //noinspection ConstantValue
        if (CollectionUtils.isEmpty(this.handlerInterceptors) || Objects.isNull(registry))
            return;
        for (HandlerInterceptor interceptor : handlerInterceptors) {
            registry.addInterceptor(interceptor).addPathPatterns("/**");
        }
    }
}