package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** ui 事件 payload — 动态 UI 卡片 */
@Data
@NoArgsConstructor
public class UiPayload {
    private String uiType;
    private String title;
    private List<Prop> props;
    private List<Action> actions;

    @JsonCreator
    public UiPayload(@JsonProperty("uiType") String uiType,
                      @JsonProperty("title") String title,
                      @JsonProperty("fields") List<Prop> props,
                      @JsonProperty("actions") List<Action> actions) {
        this.uiType = uiType; this.title = title;
        this.props = props; this.actions = actions;
    }

    @JsonProperty("fields") public List<Prop> getProps() { return props; }
    @JsonProperty("fields") public void setProps(List<Prop> props) { this.props = props; }

    @Data @NoArgsConstructor
    public static class Prop {
        private String key; private String label; private Object value;
        @JsonCreator
        public Prop(@JsonProperty("key") String k, @JsonProperty("label") String l, @JsonProperty("value") Object v) {
            this.key = k; this.label = l; this.value = v;
        }
    }

    @Data @NoArgsConstructor
    public static class Action {
        private String label; private String type; private String method; private String url; private ParamMap params;
        @JsonCreator
        public Action(@JsonProperty("label") String l, @JsonProperty("type") String t,
                       @JsonProperty("method") String m, @JsonProperty("url") String u,
                       @JsonProperty("params") ParamMap p) {
            this.label = l; this.type = t; this.method = m; this.url = u; this.params = p;
        }
    }

    @Data @NoArgsConstructor @Accessors(chain = true)
    public static class ParamMap {
        private final LinkedHashMap<String, Object> entries = new LinkedHashMap<>();
        @JsonCreator
        public ParamMap(@JsonProperty("entries") Map<String, Object> map) { if (map != null) entries.putAll(map); }
        public ParamMap put(String key, Object value) { entries.put(key, value); return this; }
        public Object get(String key) { return entries.get(key); }
        @JsonAnyGetter public Map<String, Object> any() { return entries; }
        @JsonAnySetter public void set(String key, Object value) { entries.put(key, value); }
        public static ParamMap of(String k1, Object v1) { return new ParamMap().put(k1, v1); }
        public static ParamMap of(String k1, Object v1, String k2, Object v2) {
            return new ParamMap().put(k1, v1).put(k2, v2);
        }
    }
}
