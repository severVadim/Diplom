package com.diplom.model;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ConfigurationModel {
    private List<DBModel> databases;
    private List<String> types;
}
