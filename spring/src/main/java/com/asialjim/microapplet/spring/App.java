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

package com.asialjim.microapplet.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Role;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Hooks;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 应用启动器与 ApplicationContext 持有器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@Slf4j
@Configuration
@Component(App.name)
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
@EnableAspectJAutoProxy(exposeProxy = true)
public class App implements ApplicationContextAware {
    public static final String name = "innerAppCtx";
    private static volatile ApplicationContext applicationContext;
    private static volatile ConfigurableApplicationContext configurableApplicationContext;

    @Override
    public void setApplicationContext(@SuppressWarnings("NullableProblems") ApplicationContext applicationContext) throws BeansException {
        //noinspection ConstantValue
        if (Objects.isNull(App.applicationContext) && Objects.nonNull(applicationContext))
            App.applicationContext = applicationContext;
    }

    // ========== 启动方法 ==========

    /**
     * 启动应用
     */
    @SuppressWarnings("UnusedReturnValue")
    public static ConfigurableApplicationContext start(Class<?> appClass, String[] args) {
        Hooks.enableAutomaticContextPropagation();
        ConfigurableApplicationContext ctx = SpringApplication.run(appClass, args);
        if (Objects.isNull(App.configurableApplicationContext)) {
            synchronized (App.class) {
                if (Objects.isNull(App.configurableApplicationContext))
                    App.configurableApplicationContext = ctx;
            }
        }
        return ctx;
    }

    /**
     * 启动应用（指定应用名）
     */
    public static ApplicationContext start(String appName, Class<?> sourceClass, String[] args) {
        return start(appName, StringUtils.EMPTY, sourceClass, args);
    }

    /**
     * 启动应用（指定应用名和上下文路径）
     */
    public static ApplicationContext start(String appName, String contextPath, Class<?> sourceClass, String[] args) {
        try {
            // 设置 JVM 默认时区为东八区
            TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));
            SpringApplicationBuilder builder = new SpringApplicationBuilder();
            Properties properties = new Properties();
            if (StringUtils.isNotBlank(appName))
                properties.setProperty("spring.application.name", appName);
            if (StringUtils.isNotBlank(contextPath))
                properties.setProperty("server.servlet.context-path", appName);
            if (StringUtils.isNotBlank(contextPath))
                properties.setProperty("spring.webflux.base-path", appName);

            App.applicationContext = builder.properties(properties).sources(sourceClass).run(args);
            return App.applicationContext;
        } catch (Throwable t) {
            System.err.println("\r\n\tApplication Start Failure: " + t.getMessage());
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            throw t;
        }
    }

    /**
     * 启动应用（无返回值）
     */
    public static void voidStart(Class<?> appClass, String[] args) {
        start(appClass, args);
    }
    public static void voidStart(Class<?> appClass) {
        start(appClass, new String[0]);
    }

    public static void voidStart(String appName, Class<?> sourceClass, String[] args) {
        start(appName, sourceClass, args);
    }

    public static void voidStart(String appName, String contextPath, Class<?> sourceClass, String[] args) {
        start(appName, contextPath, sourceClass, args);
    }

    // ========== 上下文获取 ==========

    public static ApplicationContext applicationContext() {
        return Optional.ofNullable(applicationContext)
                .orElseThrow(() -> new IllegalStateException("当前应用尚未初始化完成或不是通过 " + App.class.getName() + " 启动"));
    }

    public static ConfigurableApplicationContext configurableApplicationContext() {
        return Optional.ofNullable(configurableApplicationContext)
                .orElseThrow(() -> new IllegalStateException("当前应用尚未初始化完成或不是通过 " + App.class.getName() + " 启动"));
    }

    // ========== Bean 工具方法 ==========

    public static Set<String> beanNames(Class<?> beanClass) {
        String[] names = applicationContext().getBeanNamesForType(beanClass);
        return new HashSet<>(Arrays.asList(names));
    }

    public static <T> Optional<T> beanOpt(Class<T> beanClass) {
        try {
            Set<String> names = beanNames(beanClass);
            if (CollectionUtils.isEmpty(names))
                return Optional.empty();
            if (names.size() > 1)
                throw new IllegalStateException("在容器中存在多个类型为 [" + beanClass.getName() + "] 的实例，请使用名称获取");
            T bean = applicationContext().getBean(beanClass);
            return Optional.of(bean);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> beanOpt(String name, Class<T> beanClass) {
        try {
            Set<String> names = beanNames(beanClass);
            if (CollectionUtils.isEmpty(names))
                return Optional.empty();
            if (!names.contains(name))
                return Optional.empty();
            return Optional.of(applicationContext().getBean(name, beanClass));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> Optional<T> anyBeanOpt(Class<T> beanClass) {
        try {
            Set<String> names = beanNames(beanClass);
            if (CollectionUtils.isEmpty(names))
                return Optional.empty();
            for (String name : names)
                return Optional.of(applicationContext().getBean(name, beanClass));
            T bean = applicationContext().getBean(beanClass);
            return Optional.of(bean);
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public static <T> T beanOrNull(String name, Class<T> clazz) {
        return beanOpt(name, clazz).orElse(null);
    }

    public static <T> T beanOrNull(Class<T> clazz) {
        return beanOpt(clazz).orElse(null);
    }

    public static <T> T beanOrThrow(String name, Class<T> clazz, Supplier<? extends RuntimeException> supplier) {
        return beanOpt(name, clazz).orElseThrow(supplier);
    }

    public static <T> T beanOrThrow(Class<T> clazz, Supplier<? extends RuntimeException> supplier) {
        return beanOpt(clazz).orElseThrow(supplier);
    }

    public static <T> T beanAndThen(String name, Class<T> clazz, Consumer<T> consumer) {
        T t = beanOrNull(name, clazz);
        if (Objects.nonNull(t))
            consumer.accept(t);
        return t;
    }

    public static <T> T beanAndThen(Class<T> clazz, Consumer<T> consumer) {
        T t = beanOrNull(clazz);
        if (Objects.nonNull(t))
            consumer.accept(t);
        return t;
    }

    public static <T> List<T> beans(Class<T> clazz) {
        Set<String> names = beanNames(clazz);
        if (CollectionUtils.isEmpty(names))
            return Collections.emptyList();
        return names.stream().map(item -> applicationContext().getBean(item, clazz)).collect(Collectors.toList());
    }

    @SuppressWarnings("UnusedReturnValue")
    public static <T> T beanAndThenOrThrow(Class<T> clazz, Consumer<T> consumer, Supplier<? extends RuntimeException> supplier) {
        Optional<T> t = beanOpt(clazz);
        if (t.isEmpty()) {
            if (Objects.nonNull(supplier))
                throw supplier.get();
            return null;
        } else {
            t.ifPresent(consumer);
        }
        return t.get();
    }

    // ========== 事件发布 ==========

    public static void publish(Object event) {
        if (Objects.isNull(event))
            return;
        if (Objects.isNull(App.applicationContext)) {
            log.warn("当前应用尚未初始化完成或不是通过 {} 启动，无法发布事件", App.class.getName());
            return;
        }
        App.applicationContext.publishEvent(event);
    }
}
