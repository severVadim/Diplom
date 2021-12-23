package com.diplom.repository.mongo;


import com.diplom.model.DB;
import com.diplom.model.StatementExpresion;
import com.diplom.model.StatementModel;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.repository.RepositoryService;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MongoDbDao implements RepositoryService {

    private MongoCollection<Document> mycollection;

    @Autowired
    public MongoDbDao() {
        MongoClientSettings build = MongoClientSettings.builder()
                .credential(MongoCredential.createCredential("root", "test", "example".toCharArray())).build();

        MongoOperations mongoOps = new MongoTemplate(MongoClients.create(build), "test");
        mycollection = mongoOps.getCollection("mycollection");
    }


    @Override
    public void createTable(List<RequestModel.Column> columns) {

    }

    @Override
    public DBResponse fillTable(List<String> entities) {
        long time = System.currentTimeMillis();
        mycollection.drop();
        mycollection.insertMany(entities.stream().map(Document::parse).collect(Collectors.toList()));
        return DBResponse.builder()
                .dbName(DB.valueOf(getDataBase()).getDb())
                .numberOfEntities(entities.size())
                .responseTime(System.currentTimeMillis() - time).build();
    }

    @Override
    public String getDataBase() {
        return DB.MONGODB.name();
    }

    @Override
    public DBResponse getWithLimit(Integer limit) {
        long time = System.currentTimeMillis();
        MongoCursor<Document> iterator = mycollection.find().limit(limit).iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            iterator.next();
        }
        time = System.currentTimeMillis() - time;
        return DBResponse.builder().dbName(DB.valueOf(getDataBase()).getDb()).responseTime(time)
                .numberOfEntities(i).build();
    }

    @Override
    public DBResponse getWithStatement(StatementModel statement) {
        long time = System.currentTimeMillis();
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(statement.getRequestModel().getColumn().getName(), new BasicDBObject(getExpression(statement.getStatementExpresion()), statement.getValue()));
        MongoCursor<Document> iterator = mycollection.find(whereQuery).iterator();
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            iterator.next();
        }
        time = System.currentTimeMillis() - time;

        return DBResponse.builder().dbName(DB.valueOf(getDataBase()).getDb()).responseTime(time).numberOfEntities(i).build();
    }

    @Override
    public String getExpression(StatementExpresion statementExpresion) {
        if (statementExpresion.equals(StatementExpresion.EQUAL)) {
            return "$eq";
        }
        return "$gt";
    }
}
