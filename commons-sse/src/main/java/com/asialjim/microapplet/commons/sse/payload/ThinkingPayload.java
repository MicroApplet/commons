package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ThinkingPayload {
    private String text;

    @JsonCreator
    public ThinkingPayload(@JsonProperty("text") String text) { this.text = text; }
}
