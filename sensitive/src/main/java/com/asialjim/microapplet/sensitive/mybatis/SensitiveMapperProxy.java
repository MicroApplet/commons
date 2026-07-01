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

import com.asialjim.microapplet.sensitive.annotation.Sensitive;
import com.asialjim.microapplet.sensitive.mybatis.enc.StoreCipher;
import com.asialjim.microapplet.sensitive.mybatis.enc.StoreEncrypt;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.ObjectProvider;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Objects;
import java.util.function.Supplier;

public class SensitiveMapperProxy implements InvocationHandler {
    private final Object target;           // 原生 MapperProxy
    private final StoreCipher storeCipher;
    private final SensitiveBeanCopyUtils sensitiveBeanCopyUtils;

    public SensitiveMapperProxy(Object target, StoreCipher storeCipher) {
        this.target = target;
        this.storeCipher = storeCipher;
        this.sensitiveBeanCopyUtils = new SensitiveBeanCopyUtils(storeCipher);
    }

    public SensitiveMapperProxy(Object target, Supplier<StoreCipher> storeCipherObjectProvider){
        this.target = target;
        this.storeCipher = storeCipherObjectProvider.get();
        this.sensitiveBeanCopyUtils = new SensitiveBeanCopyUtils(storeCipher);

    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 1. 参数加密
        Object[] encryptedArgs = encryptArgs(method, args);
        // 2. 调用原生 Mapper
        Object result = method.invoke(target, encryptedArgs);
        // 3. 结果解密
        return decryptResult(method, result);
    }

    // ---------- 参数加密处理 ----------
    private Object[] encryptArgs(Method method, Object[] args) {
        if (ArrayUtils.isEmpty(args))
            return new Object[0];

        Parameter[] parameters = method.getParameters();
        Object[] newArgs = new Object[args.length];

        for (int i = 0; i < args.length; i++) {
            newArgs[i] = encrypt(args[i], parameters[i]);
        }

        return newArgs;
    }

    private Object encrypt(Object arg, Parameter parameter) {
        if (Objects.isNull(arg))
            //noinspection ConstantValue
            return arg;
        if (arg instanceof String str) {
            StoreEncrypt annotation = parameter.getAnnotation(StoreEncrypt.class);
            if (Objects.nonNull(annotation))
                return this.storeCipher.encrypt(str);
            return str;
        }

        return sensitiveBeanCopyUtils.encrypt(arg);
    }

    // ---------- 结果解密处理 ----------
    private Object decryptResult(Method method, Object result) {
        if (result == null)
            //noinspection ConstantValue
            return result;
        if (result instanceof String str) {
            StoreEncrypt annotation = method.getAnnotation(StoreEncrypt.class);
            if (Objects.nonNull(annotation))
                return this.storeCipher.decrypt(str);
            return str;
        }

        return sensitiveBeanCopyUtils.decrypt(result);
    }
}