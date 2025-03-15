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

package io.github.microapplet.commons.config.core;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Iterator;

/**
 * 环境信息
 *
 * @author <a href="mailto:asialjim@hotmail.com">Asial Jim</a>
 * @version 1.0
 * @since 2025/3/10, &nbsp;&nbsp; <em>version:1.0</em>
 */
@Getter
@AllArgsConstructor
public enum Env implements Iterator<Env> {

    Init(-2, "初始化环境") {
        @Override
        public Env next() {
            return null;
        }
    },

    Dr(-1, "灾备") {
        @Override
        public Env next() {
            return null;
        }
    },

    Prod(0, "生产") {
        @Override
        public boolean hasNext() {
            return true;
        }

        @Override
        public Env next() {
            return Dr;
        }
    },

    Gray(1, "灰度/小比例用户开放新版本") {
        @Override
        public Env next() {
            return Prod;
        }
    },

    Beta(2, "预发布环境/UAT[用户验收环境]") {
        @Override
        public Env next() {
            return null;
        }
    },

    Perf(3, "性能测试环境") {
        @Override
        public Env next() {
            return Beta;
        }
    },

    Test(4, "测试环境") {
        @Override
        public Env next() {
            return Perf;
        }
    },

    Dev(5, "开发环境") {
        @Override
        public Env next() {
            return Test;
        }
    },

    Local(6, "本地环境") {
        @Override
        public Env next() {
            return Dev;
        }
    };

    private final int code;
    private final String desc;

    public static Env nameOf(String property) {
        return Arrays.stream(values()).filter(item -> StringUtils.equalsIgnoreCase(item.name(), property))
                .findFirst().orElse(Prod);
    }

    @Override
    public boolean hasNext() {
        return getCode() > 0;
    }
}