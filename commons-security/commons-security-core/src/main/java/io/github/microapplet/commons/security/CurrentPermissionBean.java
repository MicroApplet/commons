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

package io.github.microapplet.commons.security;

import lombok.Setter;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * 获取当前登录用户具有的权限
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Setter
@Component(CurrentPermissionBean.CURRENT_PERMISSION_BEAN)
public class CurrentPermissionBean implements ApplicationContextAware {
    public static final String CURRENT_PERMISSION_BEAN = "innerCurrentPermissionBean";
    private ApplicationContext applicationContext;

    public CurrentPermission currentPermission(){
        String[] names = this.applicationContext.getBeanNamesForType(CurrentPermission.class);
        if (ArrayUtils.isEmpty(names))
            return Collections::emptyList;

        for (String name : names) {
            return this.applicationContext.getBean(name, CurrentPermission.class);
        }

        return Collections::emptyList;
    }
}