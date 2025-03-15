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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 获取当前用户持有的角色
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/11, &nbsp;&nbsp; <em>version:1.0</em>
 */
public interface CurrentRoles {

    /**
     * 获取当前用户持有的角色
     *
     * @return {@link List<Role>}
     * @since 2025/3/11
     */
    List<Role> hasRole();

    /**
     * 当前用户持有的角色编号列表
     *
     * @return {@link List<Long>}
     * @since 2025/3/11
     */
    default List<Long> roleCodes() {
        List<Role> roles = Optional.ofNullable(hasRole()).orElseGet(ArrayList::new);
        return roles.stream().map(Role::getCode).collect(Collectors.toList());
    }

    /**
     * 此方法仅对角色编号采用位运算标记的情况
     */
    default long totalRoleCode() {
        long code = 0;
        for (Long roleCode : roleCodes()) {
            code += roleCode;
        }
        return code;
    }
}