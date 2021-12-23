package com.diplom.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
public enum DB {
    APACHE_CASSANDRA("Apache Cassandra"),
    MONGODB("MongoDB");
    private String db;

    public static List<DBModel> getDBModels() {
        return Arrays.stream(DB.values())
                .map(dbEnum -> new DBModel(dbEnum.name(), dbEnum.getDb()))
                .collect(Collectors.toList());
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class DBModel {
        private String guid;
        private String name;
    }
}
