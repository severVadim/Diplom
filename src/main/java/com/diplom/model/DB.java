package com.diplom.model;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum DB {
    APACHE_CASSANDRA ("Apache Cassandra"),
    REDIS("Redis");
    private String db;
}
