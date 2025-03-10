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

package io.github.microapplet.common.event;

import org.springframework.aop.support.AopUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import lombok.Setter;
import org.springframework.util.CollectionUtils;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 事件工具
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/2/27, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Setter
@Component
@SuppressWarnings("rawtypes")
public class EventUtil implements ApplicationContextAware, InitializingBean {
    private static final String LISTENER_CLASS_NAME = Listener.class.getName();
    private static final Map<Type, Set<Listener>> listenerHub = new HashMap<>();
    private ApplicationContext applicationContext;

    private static void add(Type type, Listener listener) {
        if (Objects.isNull(type) || Objects.isNull(listener))
            return;

        Set<Listener> listeners = listenerHub.get(type);
        if (Objects.isNull(listeners)) {
            listeners = new HashSet<>();
            listenerHub.put(type, listeners);
        }
        listeners.add(listener);
    }

    @Override
    public void afterPropertiesSet() {
        String[] names = applicationContext.getBeanNamesForType(Listener.class);
        for (String name : names) {
            Listener bean = applicationContext.getBean(name, Listener.class);
            Class beanClass = bean.getClass();
            if (AopUtils.isAopProxy(bean)) {
                beanClass = AopUtils.getTargetClass(bean);
            }

            Type[] genericInterfaces = beanClass.getGenericInterfaces();
            for (Type type : genericInterfaces) {
                if (Objects.isNull(type))
                    continue;

                if (!(type instanceof ParameterizedType))
                    continue;

                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type rawType = parameterizedType.getRawType();
                if (Objects.isNull(rawType))
                    continue;
                String typeName = rawType.getTypeName();
                if (!LISTENER_CLASS_NAME.equals(typeName))
                    continue;

                // 获取接口的泛型参数
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                //noinspection RedundantLengthCheck
                if (Objects.isNull(actualTypeArguments) || actualTypeArguments.length == 0)
                    continue;

                for (Type actualTypeArgument : actualTypeArguments) {
                    add(actualTypeArgument, bean);
                }
            }

        }
    }

    public static <E> void push(Optional<E> event) {
        event.ifPresent(EventUtil::push);
    }

    public static <E> E push(E event) {
        if (Objects.isNull(event))
            //noinspection ConstantValue
            return event;
        Class<?> aClass = event.getClass();

        Set<Listener> listeners = listenerHub.get(aClass);
        if (CollectionUtils.isEmpty(listeners))
            return event;
        for (Listener listener : listeners) {
            //noinspection unchecked
            listener.onEvent(event);
        }
        return event;
    }
}