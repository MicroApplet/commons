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

package com.asialjim.microapplet.web.client.registrar;

import com.asialjim.microapplet.spring.App;
import com.asialjim.microapplet.web.client.adapter.HttpExchangeAdapterFactory;
import com.asialjim.microapplet.web.client.adapter.RestClientHttpExchangeAdapterFactory;
import com.asialjim.microapplet.web.client.adapter.WebClientHttpExchangeAdapterFactory;
import com.asialjim.microapplet.web.client.annotation.EnableHttpExchangeClients;
import com.asialjim.microapplet.web.client.annotation.HttpExchangeClient;
import io.micrometer.common.util.StringUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.web.server.context.WebServerApplicationContext;
import org.springframework.boot.web.server.reactive.context.ReactiveWebServerApplicationContext;
import org.springframework.boot.web.server.servlet.context.ServletWebServerApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.util.ClassUtils;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

public class EnableHttpExchangeClientsRegistrar implements ImportBeanDefinitionRegistrar {
    private static final Logger log = LoggerFactory.getLogger(EnableHttpExchangeClientsRegistrar.class);
    private static final Set<String> CLASSES = new HashSet<>();
    private static final ResourcePatternResolver RESOLVER = new PathMatchingResourcePatternResolver();
    private static final MetadataReaderFactory METADATA_READER_FACTORY = new CachingMetadataReaderFactory();
    private static final Environment ENVIRONMENT = new StandardEnvironment();
    private static final Map<String, Resource[]> resourceCache = new HashMap<>();

    @Override
    @SuppressWarnings("NullableProblems")
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
        final String configClassName = metadata.getClassName();
        final Set<String> packages = new HashSet<>();

        try {
            selectHttpExchangeClass(Class.forName(configClassName), packages);
        } catch (ClassNotFoundException ignored) {
            // do nothing here
        }

        for (String aPackage : packages) {
            processPackage(registry, aPackage);
        }
    }

    private void processPackage(BeanDefinitionRegistry registry, String aPackage) {
        try {
            Resource[] resources = scanResources(aPackage);
            for (Resource resource : resources) {
                processResource(registry, resource);
            }
        } catch (IOException e) {
            log.warn("Resolve package: {} to Find Remote Client Classes Exception Happen: {}", aPackage, e.getMessage(), e);
        }
    }

    private Resource[] scanResources(String aPackage) throws IOException {
        String resourcePattern = "**/*.class";
        String packageSearchPath = "classpath*:" + ClassUtils.convertClassNameToResourcePath(ENVIRONMENT.resolveRequiredPlaceholders(aPackage)) + "/" + resourcePattern;
        Resource[] resources = resourceCache.get(packageSearchPath);
        if (Objects.isNull(resources)) {
            synchronized (RESOLVER) {
                //noinspection ConstantValue
                if (Objects.isNull(resources)) resources = RESOLVER.getResources(packageSearchPath);
                resourceCache.put(packageSearchPath, resources);
            }
        }
        return resources;
    }

    private void processResource(BeanDefinitionRegistry registry, Resource resource) throws IOException {
        String className = candidateClassName(resource);
        if (StringUtils.isBlank(className)) return;

        try {
            Class<?> aClass = Class.forName(className);
            boolean candidateClass = candidateClass(aClass);
            if (!candidateClass) return;

            if (CLASSES.contains(className)) return;
            CLASSES.add(className);

            GenericBeanDefinition bd = new GenericBeanDefinition();
            bd.setBeanClass(aClass);
            bd.setLazyInit(true);
            bd.setDependsOn(App.name);
            bd.setInstanceSupplier(() -> {
                if (log.isDebugEnabled()) log.debug("Load Cloud HTTP Exchange Client {}...", className);
                ApplicationContext applicationContext = App.applicationContext();
                Class<? extends HttpExchangeAdapterFactory> factoryClass =
                        isReactive(applicationContext)
                                .map(item -> item ? WebClientHttpExchangeAdapterFactory.class : RestClientHttpExchangeAdapterFactory.class)
                                .orElse(RestClientHttpExchangeAdapterFactory.class);

                HttpExchangeClient client = aClass.getAnnotation(HttpExchangeClient.class);
                HttpExchangeAdapterFactory adapterFactory = applicationContext.getBean(factoryClass);
                HttpExchangeAdapter adapter = adapterFactory.build(client.value());
                HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();
                if (log.isDebugEnabled())
                    log.debug("""
                                    \rCloud HTTP Exchange
                                    \tClient: {}
                                    \tFactory: {}
                                    \tAdapter: {}""",
                            aClass.getSimpleName(), factoryClass.getSimpleName(), adapter.getClass().getSimpleName());
                return factory.createClient(aClass);
            });
            registry.registerBeanDefinition(className, bd);
        } catch (ClassNotFoundException e) {
            log.error("Cannot Found Class: {}, Exception: {}", className, e.getMessage(), e);
        }
    }

    private Optional<Boolean> isReactive(ApplicationContext applicationContext) {
        // 响应式环境？
        WebServerApplicationContext webCtx = null;
        if (applicationContext instanceof WebServerApplicationContext webApp) {
            webCtx = webApp;
        } else {
            String[] names = applicationContext.getBeanNamesForType(WebServerApplicationContext.class);
            //noinspection RedundantLengthCheck
            if (names.length > 0) {
                for (String name : names) {
                    webCtx = applicationContext.getBean(name, WebServerApplicationContext.class);
                    break;
                }
            }
        }

        if (Objects.nonNull(webCtx)) {
           return switch (webCtx) {
               case ServletWebServerApplicationContext servlet -> Optional.of(false);
               case ReactiveWebServerApplicationContext netty -> Optional.of(true);
               default -> Optional.empty();
           };
        }
        return Optional.empty();
    }


    private void selectHttpExchangeClass(Class<?> sourceClass, Set<String> packages) {
        String packageName = sourceClass.getPackageName();
        selectHttpExchangeClass(packageName, sourceClass, packages);
    }

    private void selectHttpExchangeClass(String packageName, Class<?> sourceClass, Set<String> packages) {
        Class<?> superclass = sourceClass.getSuperclass();
        if (!Object.class.equals(superclass)) selectHttpExchangeClass(packageName, superclass, packages);

        Annotation[] annotations = sourceClass.getAnnotations();
        for (Annotation annotation : annotations) {
            selectHttpExchangeClass(packageName, annotation, packages);
        }
    }

    private void selectHttpExchangeClass(String packageName, Annotation annotation, Set<String> packages) {
        if (Objects.isNull(annotation)) return;
        if (annotation.annotationType().getName().startsWith("java.lang.annotation")) return;
        if (!(annotation instanceof EnableHttpExchangeClients clients)) return;


        String[] value = clients.value();
        if (ArrayUtils.isNotEmpty(value)) {
            List<String> list = Arrays.stream(value).filter(StringUtils::isNotBlank).toList();
            if (CollectionUtils.isEmpty(list)) {
                packages.add(packageName);
            } else {
                packages.addAll(list);
            }
        } else {
            packages.add(packageName);
        }
    }

    private boolean candidateClass(Class<?> sourceClass) {
        if (Objects.isNull(sourceClass)) return false;
        Annotation[] annotations = sourceClass.getAnnotations();
        for (Annotation annotation : annotations) {
            boolean b = candidateAnnotation(annotation);
            if (b) return true;
        }

        Class<?> superclass = sourceClass.getSuperclass();
        if (Objects.nonNull(superclass)) return candidateClass(superclass);

        return false;
    }

    private boolean candidateAnnotation(Annotation annotation) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (annotationType.getName().startsWith("java.lang.annotation")) return false;

        return HttpExchangeClient.class.equals(annotationType);
    }

    private String candidateClassName(Resource resource) throws IOException {
        MetadataReader metadataReader = METADATA_READER_FACTORY.getMetadataReader(resource);
        AnnotationMetadata annotationMetadata = metadataReader.getAnnotationMetadata();
        //noinspection ConstantValue
        if (Objects.isNull(annotationMetadata)) return null;
        if (annotationMetadata.isAnnotation()) return null;
        if (!annotationMetadata.isInterface()) return null;
        ClassMetadata classMetadata = metadataReader.getClassMetadata();
        String className = classMetadata.getClassName();
        if (StringUtils.isBlank(className)) return null;
        return className;
    }
}
