package com.diplom.model.api;

import com.diplom.model.DB;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConfigurationModel {
    private List<DB.DBModel> databases;
    private List<String> types;
}
