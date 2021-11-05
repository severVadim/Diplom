package com.diplom.repository;

import com.diplom.model.DB;
import com.diplom.model.DBResponse;
import com.diplom.model.cassandra.Column;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public interface RepositoryService {
    void createTable(List<Column> columns);
    DBResponse fillTable (List<String> entities);
    String getDataBase();
}
