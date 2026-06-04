package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TextPayload {
    private String text;
    private String tts;

    public TextPayload(String text) { this.text = text; this.tts = text; }

    @JsonCreator
    public TextPayload(@JsonProperty("text") String text, @JsonProperty("tts") String tts) {
        this.text = text; this.tts = tts;
    }
}
