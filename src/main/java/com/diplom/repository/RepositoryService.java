package com.diplom.repository;

import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;

import java.util.List;

public interface RepositoryService {
    void createTable(List<RequestModel.Column> columns);
    DBResponse fillTable (List<String> entities);
    String getDataBase();
    DBResponse getWithLimit(Integer limit);
    DBResponse getWithStatement(String statement);
}
