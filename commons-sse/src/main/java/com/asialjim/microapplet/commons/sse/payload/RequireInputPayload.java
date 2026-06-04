package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequireInputPayload<R> {
    private String question;
    private String hint;
    private Class<R> responseType;

    public RequireInputPayload(String question, String hint) { this.question = question; this.hint = hint; }

    @JsonCreator
    public RequireInputPayload(@JsonProperty("question") String question,
                                @JsonProperty("hint") String hint,
                                @JsonProperty("responseType") Class<R> responseType) {
        this.question = question; this.hint = hint; this.responseType = responseType;
    }
}
