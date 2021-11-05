package com.diplom.model;

import com.diplom.model.cassandra.Column;
import lombok.AllArgsConstructor;
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
        private List<Object> values;
    }

}
