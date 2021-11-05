package com.diplom.model.cassandra;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Type {

    TEXT ("text"),
    LONG("bigint"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    INT("int"),
    DATE("date");
    private String type;

}
