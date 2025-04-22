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

package com.asialjim.microapplet.web.flux.router;

import lombok.Setter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;


/**
 * 门户配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/24, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Setter
@Configuration
public class IndexConfiguration implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Bean
    public RouterFunction<ServerResponse> mixRouter() {
        return RouterFunctions.route(RequestPredicates.GET("/index"), serverRequest -> ServerResponse
                        .ok()
                        .header("X-Response-Code", "200")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue(index())))
                .andRoute(RequestPredicates.GET("/health"), serverRequest -> ServerResponse
                        .ok()
                        .header("X-Response-Code", "200")
                        .contentType(MediaType.TEXT_PLAIN)
                        .body(BodyInserters.fromValue("200")));

    }

    private String index() {
        return String.format("%s Index", applicationContext.getApplicationName());
    }
}