package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorPayload {
    private String code;
    private String message;

    @JsonCreator
    public ErrorPayload(@JsonProperty("code") String code, @JsonProperty("message") String message) {
        this.code = code; this.message = message;
    }
}
