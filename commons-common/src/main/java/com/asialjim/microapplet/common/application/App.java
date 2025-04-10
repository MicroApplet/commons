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

package com.asialjim.microapplet.common.application;

import com.asialjim.microapplet.common.event.EventUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 应用启动器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/26, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class App {
    public static ConfigurableApplicationContext ctx;

    public static Set<String> beanNames(Class<?> clazz) {
        String[] names = ctx.getBeanNamesForType(clazz);
        return new HashSet<>(Arrays.asList(names));
    }

    public static <T> T beanOrNull(String name, Class<T> clazz) {
        return beanOpt(name, clazz).orElse(null);
    }

    public static <T> T beanOrNull(Class<T> clazz) {
        return beanOpt(clazz).orElse(null);
    }

    public static <T> T beanOrThrow(String name, Class<T> clazz, Supplier<? extends RuntimeException> supplier) {
        //noinspection ConstantValue
        return beanOpt(name, clazz).filter(Objects::nonNull).orElseThrow(supplier);
    }

    public static <T> T beanOrThrow(Class<T> clazz, Supplier<? extends RuntimeException> supplier) {
        //noinspection ConstantValue
        return beanOpt(clazz).filter(Objects::nonNull).orElseThrow(supplier);
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

    public static <T> Optional<T> beanOpt(String name, Class<T> clazz) {
        Set<String> names = beanNames(clazz);
        if (CollectionUtils.isEmpty(names))
            return Optional.empty();
        if (!names.contains(name))
            return Optional.empty();

        T bean = ctx.getBean(name, clazz);
        return Optional.of(bean);
    }

    public static <T> Optional<T> beanOpt(Class<T> clazz) {
        Set<String> names = beanNames(clazz);
        if (CollectionUtils.isEmpty(names))
            return Optional.empty();
        for (String name : names) {
            T bean = ctx.getBean(name, clazz);
            return Optional.of(bean);
        }

        return Optional.empty();
    }

    public static <T> List<T> beans(Class<T> clazz) {
        Set<String> names = beanNames(clazz);
        if (CollectionUtils.isEmpty(names))
            return Collections.emptyList();

        return names.stream().map(item -> ctx.getBean(item, clazz)).collect(Collectors.toList());
    }

    /**
     * 启动应用
     */
    public static void voidStart(Class<?> sourceClass, String[] args) {
        //noinspection unused
        AppStarted start = start(sourceClass, args);
    }

    /**
     * 启动应用
     */
    public static AppStarted start(Class<?> sourceClass, String[] args) {
        try {
            App.ctx = SpringApplication.run(sourceClass, args);
            return EventUtil.push(new AppStarted(ctx));
        } catch (Throwable t) {
            System.err.println("\r\n\tApplication Start Failure: " + t.getMessage());
            //noinspection CallToPrintStackTrace
            t.printStackTrace();
            throw t;
        }
    }

    public static <T> T beanAndThenOrThrow(Class<T> clazz, Consumer<T> consumer, Supplier<? extends RuntimeException> supplier) {
        Optional<T> t = beanOpt(clazz);
        if (!t.isPresent()) {
            if (Objects.nonNull(supplier))
                throw supplier.get();
            return null;
        } else {
            t.ifPresent(consumer);
        }

        return t.get();
    }
}