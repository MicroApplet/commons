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

package com.asialjim.microapplet.common.concurrent;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * 并发应用
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/4/9, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Component
public class ConcurrentRunner {
    private static ConcurrentRunner instance;
    private Executor executor;


    public static void runAllTask(Runnable... runnable) {
        getInstance()._runAllTask(runnable);
    }

    public static void runAllTask(Executor executor, Runnable... runnable) {
        getInstance()._runAllTask(executor, runnable);
    }

    public static void runAllTaskAsync(Executor executor, Runnable... runnable) {
        getInstance()._runAllTaskAsync(executor, runnable);
    }

    private static ConcurrentRunner getInstance() {
        if (Objects.nonNull(instance))
            return instance;

        synchronized (ConcurrentRunner.class) {
            if (Objects.nonNull(instance))
                return instance;

            instance = new ConcurrentRunner();
            instance.init();
            instance.setExecutor(null);
        }
        return instance;
    }

    @PostConstruct
    public void init() {
        ConcurrentRunner.instance = this;
    }

    @Autowired
    public void setExecutor(List<Executor> executors) {
        this.executor = Optional.ofNullable(executors)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .filter(Objects::nonNull)
                .findAny()
                .orElseGet(Executors::newSingleThreadExecutor);
    }


    public void _runAllTask(Runnable... runnable) {
        _runAllTask(this.executor, runnable);
    }

    public void _runAllTask(Executor executor, Runnable... runnable) {
        if (ArrayUtils.isEmpty(runnable))
            return;
        CompletableFuture<?>[] tasks = new CompletableFuture[runnable.length];
        for (int i = 0; i < runnable.length; i++) {
            Runnable item = runnable[i];
            CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(item, executor);
            tasks[i] = voidCompletableFuture;
        }
        CompletableFuture.allOf(tasks).join();
    }

    public void _runAllTaskAsync(Executor executor, Runnable... runnable) {
        if (ArrayUtils.isEmpty(runnable))
            return;

        for (Runnable item : runnable) {
            executor.execute(item);
        }
    }
}