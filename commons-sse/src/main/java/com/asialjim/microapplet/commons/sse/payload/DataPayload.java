package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataPayload<T> {
    private Object schema;
    private List<T> data;
    private String summary;

    public DataPayload(Object schema, List<T> data) { this.schema = schema; this.data = data; }

    @JsonCreator
    public DataPayload(@JsonProperty("schema") Object schema,
                        @JsonProperty("data") List<T> data,
                        @JsonProperty("summary") String summary) {
        this.schema = schema; this.data = data; this.summary = summary;
    }
}
