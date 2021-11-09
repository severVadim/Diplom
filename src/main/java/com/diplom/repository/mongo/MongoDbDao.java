package com.diplom.repository.mongo;

import com.diplom.model.DB;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.repository.RepositoryService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MongoDbDao implements RepositoryService {
    @Override
    public void createTable(List<RequestModel.Column> columns) {

    }

    @Override
    public DBResponse fillTable(List<String> entities) {
        return  DBResponse.builder().dbName(DB.valueOf(getDataBase()).getDb()).responseTime(400L).build();
    }

    @Override
    public String getDataBase() {
        return DB.MONGODB.name();
    }

    @Override
    public DBResponse getWithLimit(Integer limit) {
        return  DBResponse.builder().dbName(DB.valueOf(getDataBase()).getDb()).responseTime(18L).build();
    }

    @Override
    public DBResponse getWithStatement(String statement) {
        return  DBResponse.builder().dbName(DB.valueOf(getDataBase()).getDb()).responseTime(22L).build();
    }
}
