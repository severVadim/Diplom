package com.diplom.model.api;

import com.diplom.model.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModel{
    private ValueRange valueRange;
    private Column column;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ValueRange {
        private float fromValue;
        private float toValue;
        private List<String> values;
    }
    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Column {
        private String name;
        private Type type;
        private boolean primaryKey;
    }

}
