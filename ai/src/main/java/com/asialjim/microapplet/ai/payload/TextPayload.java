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

package com.asialjim.microapplet.ai.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文本回复负载。
 *
 * <p>AI 以纯文本形式回复用户时使用，可附带语音合成文本用于 TTS 播报。</p>
 *
 * <pre>{@code
 * SseEvent.text("你好，有什么可以帮助您的？")
 * SseEvent.text("余额为 200 元", "您的账户余额为200元")
 * }</pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPayload {
    /** 展示文本 */
    private String text;
    /** 语音合成文本（TTS 用），为空时默认取 text */
    private String tts;

    public TextPayload(String text) {
        this.text = text;
        this.tts = text;
    }
}
