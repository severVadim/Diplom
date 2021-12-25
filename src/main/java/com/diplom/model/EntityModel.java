package com.diplom.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EntityModel {
    private String filedName;
    private Object value;
    private Class valueType;
}
