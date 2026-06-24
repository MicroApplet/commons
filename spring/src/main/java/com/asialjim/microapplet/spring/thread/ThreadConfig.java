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

package com.asialjim.microapplet.spring.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;

/**
 * 线程池配置
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 */
@EnableAsync
@Configuration
public class ThreadConfig {

    /**
     * 虚拟线程池
     */
    @Bean
    @Primary
    public ExecutorService threadExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    /**
     * 调度线程池
     */
    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return threadFactoryScheduledExecutorService("virtual-sch-");
    }

    public ScheduledExecutorService threadFactoryScheduledExecutorService(String name) {
        ThreadFactory factory = Thread.ofVirtual().name(name, 0).factory();
        return Executors.newScheduledThreadPool(
                Runtime.getRuntime().availableProcessors(),
                factory
        );
    }

    /**
     * 异步任务线程池
     */
    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(Runtime.getRuntime().availableProcessors());
        executor.setThreadFactory(Thread.ofVirtual().factory());
        executor.setThreadNamePrefix("virtual-task-");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(15);
        return executor;
    }
}
