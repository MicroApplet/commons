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

package io.github.microapplet.commons.web.knife4j;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 门面文档
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/14, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Index", version = "v1.0", description = "Index")
)
public class IndexOpenAPIDefinition {
    @Bean
    public GroupedOpenApi userIndexOpenApi() {
        String[] paths = {"/index/**"};
        return GroupedOpenApi.builder().group("Index")
                .addOpenApiCustomiser(openApi -> openApi.info(
                        new io.swagger.v3.oas.models.info.Info().title("Index API").version("v1.0")))
                .pathsToMatch(paths)
                .build();
    }
}