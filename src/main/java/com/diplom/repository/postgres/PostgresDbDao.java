package com.diplom.repository.postgres;


import com.diplom.model.DB;
import com.diplom.model.EntityModel;
import com.diplom.model.StatementExpresion;
import com.diplom.model.StatementModel;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.repository.RepositoryService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PostgresDbDao implements RepositoryService {


    private Connection connection;
    private static final String TABLE = "test_table";
    private DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @SneakyThrows
    @Autowired
    public PostgresDbDao() {
        connection = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5432/test",
                "postgres", "postgres");
    }


    @SneakyThrows
    @Override
    public void createTable(List<RequestModel.Column> columns) {
        Statement statement = connection.createStatement();
        statement.execute("DROP TABLE " + TABLE + ";");
        statement.execute(createTableQueryDynamicBuilder(columns));
        statement.close();
    }

    @SneakyThrows
    @Override
    public DBResponse fillTable(List<List<EntityModel>> entities) {
        int size = entities.get(0).size();
        String placeholder = String.join(",", Collections.nCopies(size, "(?)"));
        String query = "INSERT INTO " + TABLE + " VALUES (" + placeholder + ")";
        PreparedStatement statement = connection.prepareStatement(query);

        connection.createStatement().execute(String.format("TRUNCATE public.%s", TABLE));
        long time = System.currentTimeMillis();
        long rowCount = 0;
        for (List<EntityModel> entity : entities) {
            for (int i = 0; i < size; i++) {
                try {
                    if (entity.get(i).getValueType().equals(Date.class)) {
                        statement.setDate(i + 1, Date.valueOf(entity.get(i).getValue().toString()));
                    } else {
                        statement.setObject(i + 1, entity.get(i).getValue());
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            try {
                statement.addBatch();
                rowCount++;
                if (rowCount % 1000 == 0 || rowCount == size) {
                    statement.executeBatch();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        statement.close();
        return DBResponse.builder()
                .dbName(DB.valueOf(getDataBase()).getDb())
                .numberOfEntities(entities.size())
                .responseTime(System.currentTimeMillis() - time).build();
    }

    @Override
    public String getDataBase() {
        return DB.POSTGRES.name();
    }

    @Override
    public DBResponse getWithLimit(Integer limit) {
        return null;
    }

    @Override
    public DBResponse getWithStatement(StatementModel statement) {
        return null;
    }

    @Override
    public String getExpression(StatementExpresion statementExpresion) {
        if (statementExpresion.equals(StatementExpresion.EQUAL)) {
            return "=";
        }
        return ">";
    }


    private String createTableQueryDynamicBuilder(List<RequestModel.Column> columns) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS " + TABLE + " (");
        builder.append(columns
                .stream()
                .map(column -> column.getName() + " " + column.getType().getType())
                .collect(Collectors.joining(", ")));
        List<RequestModel.Column> primaryColumns = columns.stream().filter(RequestModel.Column::isPrimaryKey).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(primaryColumns)) {
            builder.append(", primary key (");
            builder.append(primaryColumns
                    .stream()
                    .map(RequestModel.Column::getName)
                    .collect(Collectors.joining(", ")));
            builder.append(")");
        }
        builder.append(");");
        return builder.toString();
    }
}

