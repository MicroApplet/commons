package com.asialjim.microapplet.commons.sse.payload;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * ui 事件 payload — 动态 UI 卡片
 */
@Data
public class UiPayload {
    private String uiType;
    private String title;
    private List<Prop> props;
    private List<Action> actions;

    @Data
    public static class Prop {
        private String key;
        private String label;
        private Object value;
    }

    @Data
    public static class Action {
        private String label;
        private String type;
        private String method;
        private String url;
        private ParamMap params;
    }

    @Data
    @NoArgsConstructor
    public static class ParamMap {
        private final LinkedHashMap<String, Object> entries = new LinkedHashMap<>();

        public ParamMap put(String key, Object value) {
            entries.put(key, value);
            return this;
        }

        public Object get(String key) {
            return entries.get(key);
        }

        @JsonAnyGetter
        public Map<String, Object> any() {
            return entries;
        }

        @JsonAnySetter
        public void set(String key, Object value) {
            entries.put(key, value);
        }

        public static ParamMap of(String k1, Object v1) {
            return new ParamMap().put(k1, v1);
        }

        public static ParamMap of(String k1, Object v1, String k2, Object v2) {
            return new ParamMap().put(k1, v1).put(k2, v2);
        }
    }
}