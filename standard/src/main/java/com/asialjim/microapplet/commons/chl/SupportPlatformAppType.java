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

package com.asialjim.microapplet.commons.chl;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupportPlatformAppType implements PlatformAppType {
    TikTokApplet(SupportPlatformType.DouYin, "applet", "抖音小程序"),

    WeChatOfficial(SupportPlatformType.WeChat,"official","微信公众号"),
    WeChatApplet(SupportPlatformType.WeChat, "applet", "微信小程序");

    private final PlatformType platformType;
    private final String code;
    private final String name;
}
