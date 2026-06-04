package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToolCallPayload {
    private String tool;
    private Map<String, Object> params;

    @JsonCreator
    public ToolCallPayload(@JsonProperty("tool") String tool, @JsonProperty("params") Map<String, Object> params) {
        this.tool = tool; this.params = params;
    }
}
