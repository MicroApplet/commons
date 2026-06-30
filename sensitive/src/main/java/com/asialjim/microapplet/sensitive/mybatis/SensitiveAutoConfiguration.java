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

package com.asialjim.microapplet.sensitive.mybatis;

import com.asialjim.microapplet.sensitive.mybatis.enc.StoreCipher;
import lombok.SneakyThrows;
import org.jspecify.annotations.NonNull;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.lang.reflect.Proxy;

@ComponentScan
@org.springframework.context.annotation.Configuration
public class SensitiveAutoConfiguration implements BeanPostProcessor {

    @Bean
    public BeanPostProcessor sensitiveStoreBeanPostProcessor(StoreCipher storeCipher) {
        return new BeanPostProcessor() {

            @Override
            @SneakyThrows
            public @NonNull Object postProcessAfterInitialization(
                    @SuppressWarnings("NullableProblems") Object bean,
                    @SuppressWarnings("NullableProblems") String beanName) throws BeansException {

                //noinspection rawtypes
                if (!(bean instanceof MapperFactoryBean factoryBean))
                    return bean;

                //noinspection rawtypes
                Class type = factoryBean.getMapperInterface();
                Object object = factoryBean.getObject();

                //noinspection rawtypes,unchecked
                return new MapperFactoryBean(type) {
                    @Override
                    public Object getObject() {
                        return Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, new SensitiveMapperProxy(object, storeCipher));
                    }
                };
            }
        };
    }


}