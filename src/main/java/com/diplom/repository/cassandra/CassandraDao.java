package com.diplom.repository.cassandra;


import com.datastax.driver.core.*;
import com.diplom.model.DB;
import com.diplom.model.EntityModel;
import com.diplom.model.StatementExpresion;
import com.diplom.model.StatementModel;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.repository.RepositoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.diplom.util.EntityBuilder.addSingleQuotes;


@Component
public class CassandraDao implements RepositoryService {

    private final CassandraConfiguration config;
    private final Cluster cluster;
    private final Session session;
    private static final String KEY_SPACE = "new_k";
    private static final String TABLE = "test_table";

    @Autowired
    public CassandraDao(CassandraConfiguration config) {
        this.config = config;
        this.cluster = Cluster.builder()
                .addContactPoint(config.getHost())
                .withPort(config.getPort())
                .withCredentials(config.getUser(), config.getPass())
                .build();
        this.session = cluster.connect(KEY_SPACE);
    }

    @Override
    public void createTable(List<RequestModel.Column> columns) {
        String queryDynamicBuilder = createTableQueryDynamicBuilder(columns);
        session.execute(String.format("DROP TABLE IF EXISTS %s.%s", KEY_SPACE, TABLE));
        session.execute(queryDynamicBuilder);
        session.getState();
    }

    @Override
    public DBResponse fillTable(List<List<EntityModel>> entities) {
        int columnNumber = entities.get(0).size();
        String placeholder = String.join(",", Collections.nCopies(columnNumber, "?"));
        String columns = columnPlaceHolder(entities.get(0));
        String query = String.format("INSERT INTO %s.%s (%s) VALUES (%s)", KEY_SPACE, TABLE, columns, placeholder);
        PreparedStatement preparedInsertExpense = session.prepare(query);
        session.execute(String.format("TRUNCATE %s.%s", KEY_SPACE, TABLE));

        long time = System.currentTimeMillis();
        batches(entities, 100, preparedInsertExpense);
        return DBResponse.builder()
                .dbName(DB.valueOf(getDataBase()).getDb())
                .numberOfEntities(entities.size())
                .responseTime(System.currentTimeMillis() - time).build();
    }

    @Override
    public String getDataBase() {
        return DB.APACHE_CASSANDRA.name();
    }


    @Override
    public DBResponse getWithLimit(Integer limit) {
        long time = System.currentTimeMillis();
        ResultSet result =
                session.execute(String.format("SELECT * FROM %s.%s LIMIT %s;", KEY_SPACE, TABLE, limit));
        return DBResponse.builder()
                .dbName(DB.valueOf(getDataBase()).getDb())
                .numberOfEntities(result.all().size())
                .responseTime(System.currentTimeMillis() - time).build();
    }

    @Override
    public DBResponse getWithStatement(StatementModel statement) {
        long time = System.currentTimeMillis();
        ResultSet result =
                session.execute(String.format("SELECT * FROM %s.%s WHERE %s %s %s allow filtering;", KEY_SPACE, TABLE,
                        statement.getRequestModel().getColumn().getName(), getExpression(statement.getStatementExpresion()),
                        addSingleQuotes(statement.getValue().toString(), statement.getRequestModel().getColumn().getType())));
        return DBResponse.builder()
                .dbName(DB.valueOf(getDataBase()).getDb())
                .numberOfEntities(result.all().size())
                .responseTime(System.currentTimeMillis() - time).build();
    }


    private String columnPlaceHolder(List<EntityModel> entityModel) {
        return entityModel.stream().map(EntityModel::getFiledName).collect(Collectors.joining(","));
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

    private void executeBatch(List<List<EntityModel>> entities, PreparedStatement preparedInsertExpense) {
        BatchStatement batchStatement = new BatchStatement(BatchStatement.Type.LOGGED);
        entities.forEach(entity -> batchStatement.add(preparedInsertExpense.bind(convertToList(entity))));
        session.execute(batchStatement);
    }

    public void batches(List<List<EntityModel>> entities, int length, PreparedStatement preparedInsertExpense) {
        int size = entities.size();
        int fullChunks = (size - 1) / length;
        IntStream.range(0, fullChunks + 1).forEach(
                n -> executeBatch(entities.subList(n * length, n == fullChunks ? size : (n + 1) * length), preparedInsertExpense));
    }

    public String getExpression(StatementExpresion statementExpresion) {
        if (statementExpresion.equals(StatementExpresion.EQUAL)) {
            return "=";
        }
        return ">";
    }


    @SneakyThrows
    private Object[] convertToList(List<EntityModel> entityModel) {
        return entityModel.stream().map(model -> {
            if (model.getValueType().equals(Date.class)) {
                return convertDate(model.getValue().toString());
            }
            return model.getValue();
        }).toArray();
    }

    public static LocalDate convertDate(final String date) {
        String[] arr = date.split("-");
        if (arr.length != 3)
            return null;
        return LocalDate.fromYearMonthDay(Integer.parseInt(arr[0]),
                Integer.parseInt(arr[1]),
                Integer.parseInt(arr[2]));
    }
}
