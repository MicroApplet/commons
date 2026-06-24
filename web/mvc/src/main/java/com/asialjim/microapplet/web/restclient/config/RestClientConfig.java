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

package com.asialjim.microapplet.web.restclient.config;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.AbstractJacksonHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.JacksonJsonHttpMessageConverter;
import org.springframework.http.converter.xml.JacksonXmlHttpMessageConverter;
import org.springframework.web.client.RestClient;


@Slf4j
@Configuration
public class RestClientConfig {
    @Resource
    public JacksonJsonHttpMessageConverter jacksonJsonHttpMessageConverter;

    @Resource
    public JacksonXmlHttpMessageConverter jacksonXmlHttpMessageConverter;


    @Bean
    @LoadBalanced
    @ConditionalOnMissingBean(name = "loadBalancedRestClientBuilder")
    public RestClient.Builder loadBalancedRestClientBuilder() {
        LoadBalancedHttpRequestInterceptor interceptor = new LoadBalancedHttpRequestInterceptor();
        return RestClient.builder()
                .configureMessageConverters(builder ->
                        builder.configureMessageConvertersList(list -> {
                            for (int i = 0; i < list.size(); i++) {
                                HttpMessageConverter<?> converter = list.get(i);
                                if (converter instanceof AbstractJacksonHttpMessageConverter<?> jacksonConverter) {
                                    if (jacksonConverter instanceof JacksonJsonHttpMessageConverter)
                                        list.set(i, jacksonJsonHttpMessageConverter);
                                    if (jacksonConverter instanceof JacksonXmlHttpMessageConverter)
                                        list.set(i, jacksonXmlHttpMessageConverter);
                                }
                            }
                        }))
                .requestInterceptor(interceptor);
    }


    @Bean
    @Primary
    @ConditionalOnMissingBean(name = "restClientBuilder")
    public RestClient.Builder restClientBuilder() {
        return RestClient.builder();
    }
}