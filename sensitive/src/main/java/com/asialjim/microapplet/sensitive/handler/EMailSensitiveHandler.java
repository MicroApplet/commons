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

package com.asialjim.microapplet.sensitive.handler;

import com.asialjim.microapplet.sensitive.SensitiveType;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class EMailSensitiveHandler extends SensitiveHandler {
    @Override public SensitiveType type() { return SensitiveType.EMail; }


    public Function<String, String> function() {

        return s -> {
            int index = s.indexOf('@');
            String substring = s.substring(0, index);
            String domain = s.substring(index);
            int length = substring.length();

            int prefix ;
            int suffix;
            if (length <= 1) {
                return "*" + domain;
            }
            if (length > 8){
                prefix = 3;
                suffix = 3;
            } else if (length > 5){
                prefix = 2;
                suffix = 2;
            }
            else {
                prefix = 1;
                suffix = 0;
            }

            return maskWithIndex(substring, prefix, suffix) + domain;
        };
    }
}
