/*
 *  Copyright 2014-2025 <a href="mailto:asialjim@qq.com">Asial Jim</a>
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.asialjim.microapplet.web.mvc.controller;

import com.asialjim.microapplet.common.application.AppStarted;
import com.asialjim.microapplet.common.event.Listener;
import com.asialjim.microapplet.web.mvc.annotation.ResultWrap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门面
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@RestController
@RequestMapping("/index")
public class IndexController implements Listener<AppStarted> {
    private String name;

    @ResultWrap
    @GetMapping("/index")
    public String index() {
        return name + " Index";
    }

    @Override
    public void doOnEvent(AppStarted event) {
        this.name = event.ctx.getEnvironment().getProperty("spring.application.name");
    }
}