package com.diplom.repository;

import com.diplom.model.EntityModel;
import com.diplom.model.StatementExpresion;
import com.diplom.model.StatementModel;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;

import java.util.List;

public interface RepositoryService {
    void createTable(List<RequestModel.Column> columns);

    DBResponse fillTable(List<List<EntityModel>> entities);

    String getDataBase();

    DBResponse getWithLimit(Integer limit);

    DBResponse getWithStatement(StatementModel statement);

    String getExpression(StatementExpresion statementExpresion);
}
