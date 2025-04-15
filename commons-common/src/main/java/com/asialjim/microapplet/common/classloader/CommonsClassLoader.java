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

package com.asialjim.microapplet.common.classloader;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 通用类加载器
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/15, &nbsp;&nbsp; <em>version:1.0</em>
 */
public class CommonsClassLoader extends ClassLoader {
    private static final Logger log = LoggerFactory.getLogger(CommonsClassLoader.class);
    private static final Map<String, Class<?>> CLASS_MAP = new ConcurrentHashMap<>();
    private static final String REMOTE_CLASSES_CONFIG = "META-INF/commons.loadClass";
    private static CommonsClassLoader instance;
    private static ClassLoader classLoader;
    private static volatile boolean init = false;

    public static CommonsClassLoader getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (CommonsClassLoader.class) {
                if (Objects.isNull(instance))
                    instance = new CommonsClassLoader();
            }
        }
        return instance;
    }

    public CommonsClassLoader() {
        CommonsClassLoader.instance = this;
    }

    public CommonsClassLoader(ClassLoader parent) {
        super(parent);
        CommonsClassLoader.instance = this;
    }

    public static void classLoader(ClassLoader classLoader) {
        CommonsClassLoader.classLoader = Optional.ofNullable(classLoader).orElseGet(CommonsClassLoader.class::getClassLoader);
    }

    private static ClassLoader classLoader() {
        return Optional.ofNullable(CommonsClassLoader.classLoader).orElseGet(CommonsClassLoader.class::getClassLoader);
    }

    public static void init() {
        getInstance().load();
    }

    public void load() {
        if (init)
            return;
        ClassLoader classLoader = classLoader();
        if (log.isDebugEnabled())
            log.debug("RemoteClassLoader init, Target ClassLoader: {}", classLoader);
        try {
            Enumeration<URL> resources = classLoader.getResources(REMOTE_CLASSES_CONFIG);
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                InputStream inputStream = null;
                try {
                    inputStream = url.openStream();
                    if (Objects.isNull(inputStream))
                        continue;
                    List<String> list = IOUtils.readLines(inputStream, StandardCharsets.UTF_8);
                    for (String line : list) {
                        if (StringUtils.isBlank(line) || StringUtils.startsWith(StringUtils.trim(line), "#"))
                            continue;
                        Class<?> aClass;
                        try {
                            if (log.isDebugEnabled())
                                log.info("Load Class: {}", line);
                            aClass = loadClass(line);
                            if (log.isDebugEnabled())
                                log.info("Class:{} loaded:{}", line, aClass);
                        } catch (ClassNotFoundException e) {
                            if (log.isDebugEnabled())
                                log.info("Load Class: {}, exception: {}", line, e.getMessage(), e);
                        }
                    }
                } catch (IOException e) {
                    if (log.isDebugEnabled())
                        log.info("Load Url: {}, exception: {}", url, e.getMessage(), e);
                } finally {
                    IOUtils.closeQuietly(inputStream);
                }
            }
        } catch (Throwable e) {
            if (log.isDebugEnabled())
                log.info("Load Resources: {}, exception: {}", REMOTE_CLASSES_CONFIG, e.getMessage(), e);
            throw new RuntimeException(e);
        }
        init = true;
    }

    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        Class<?> aClass = CLASS_MAP.get(name);
        if (Objects.nonNull(aClass))
            return aClass;

        try {
            aClass = classLoader().loadClass(name);
            if (Objects.nonNull(aClass)) {
                CLASS_MAP.putIfAbsent(name, aClass);
                return aClass;
            }
        } catch (Throwable t) {
            aClass = super.loadClass(name);
            CLASS_MAP.putIfAbsent(name, aClass);
        }

        return aClass;
    }
}