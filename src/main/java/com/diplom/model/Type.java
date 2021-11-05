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
public enum Type {

    TEXT ("text"),
    LONG("bigint"),
    FLOAT("float"),
    BOOLEAN("boolean"),
    INT("int"),
    DATE("date");
    private String type;

    public static List<String> getTypesNames(){
        return Arrays.stream(Type.values()).map(Enum::name).collect(Collectors.toList());
    }
}
