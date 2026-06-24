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

package com.asialjim.microapplet.web.mvc.controller;

import com.asialjim.microapplet.web.mvc.annotation.RwIgnore;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 根路径控制器 — 健康检查
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@RestController
@AllArgsConstructor
public class IndexController {

    private final ApplicationContext applicationContext;

    @RwIgnore
    @GetMapping("/index")
    public String index(){
        return this.applicationContext.getEnvironment().getProperty("spring.application.name") + " Index";
    }

    @RwIgnore
    @GetMapping(value = "/health")
    public Integer health() {
        return 200;
    }

    @GetMapping("/is-mcp-server")
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void notMcpServer(){
    }
}