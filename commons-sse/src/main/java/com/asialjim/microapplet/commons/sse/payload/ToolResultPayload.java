package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolResultPayload {
    private String tool;
    private Object result;

    @JsonCreator
    public ToolResultPayload(@JsonProperty("tool") String tool, @JsonProperty("result") Object result) {
        this.tool = tool; this.result = result;
    }
}
