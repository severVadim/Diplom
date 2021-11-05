package com.diplom.model.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DBResponse {
    private String dbName;
    private Long numberOfEntities;
    private Long responseTime;
}
