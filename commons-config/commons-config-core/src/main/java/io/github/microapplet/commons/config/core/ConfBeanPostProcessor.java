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

package io.github.microapplet.commons.config.core;

import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.Ordered;
import org.springframework.core.env.Environment;

import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * Conf 代理处理
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Setter
public class ConfBeanPostProcessor implements BeanPostProcessor, EnvironmentAware, ApplicationContextAware, Ordered {
    private ApplicationContext applicationContext;
    private Environment environment;

    private EnvProperty envProperty;
    private ConfPropertyRepository confPropertyRepository;

    @Override
    @SuppressWarnings("NullableProblems")
    public Object postProcessAfterInitialization(Object bean,
                                                 String beanName) throws BeansException {

        //noinspection ConstantValue
        if (Objects.isNull(bean))
            return bean;

        TypedConfiguration annotation = bean.getClass().getAnnotation(TypedConfiguration.class);
        if (Objects.isNull(annotation))
            return bean;

        if (!(bean instanceof Conf))
            throw new IllegalStateException("TypedConfiguration:" + bean + " must implements: " + Conf.class.getName());

        Conf<?> source = (Conf<?>) bean;
        Conf<?> defaultConf = source.deftConf();
        ConfType type = annotation.type();
        String business = annotation.business();
        String code = annotation.code();
        Env env = envProperty().getEnv();

        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(bean.getClass());
        enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
            int parameterCount = method.getParameterCount();
            if (parameterCount != 0)
                return method.invoke(source, args);

            int modifiers = method.getModifiers();
            if (Modifier.isStatic(modifiers))
                return method.invoke(source, args);

            if (Modifier.isFinal(modifiers))
                return method.invoke(source, args);

            String name = method.getName();
            boolean getMethod = StringUtils.startsWith(name, "get");
            boolean isBooleanMethod = StringUtils.startsWith(name, "is") && Boolean.class.isAssignableFrom(method.getReturnType());
            boolean instance = StringUtils.equals(name, "instance");
            boolean toString = StringUtils.equals(name, "toString");
            if (!getMethod && !isBooleanMethod && !instance)
                return method.invoke(source, args);

            ConfProperty confProperty = confPropertyRepository().queryEnable(type, business, code, env);
            Object targetObj = ConfProperty.confInstance(confProperty, bean.getClass());
            if (Objects.isNull(targetObj)) {
                targetObj = source.config(env);
                if (Objects.isNull(targetObj))
                    targetObj = defaultConf;
            }
            if (instance)
                return targetObj;
            if (toString)
                return targetObj.toString();

            return method.invoke(targetObj, args);
        });
        return enhancer.create();
    }

    private ConfPropertyRepository confPropertyRepository() {
        if (Objects.isNull(this.confPropertyRepository)) {
            synchronized (ConfBeanPostProcessor.class) {
                if (Objects.isNull(this.confPropertyRepository)) {
                    this.confPropertyRepository = applicationContext.getBean(ConfPropertyRepository.class);
                }
            }
        }
        return this.confPropertyRepository;
    }

    private EnvProperty envProperty() {
        if (Objects.isNull(envProperty)) {
            synchronized (ConfBeanPostProcessor.class) {
                if (Objects.isNull(envProperty)) {
                    String property = this.environment.getProperty(EnvProperty.envKey);
                    Env env = Env.nameOf(property);
                    this.envProperty = new EnvProperty();
                    this.envProperty.setEnv(env);
                }
            }
        }
        return envProperty;
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }
}