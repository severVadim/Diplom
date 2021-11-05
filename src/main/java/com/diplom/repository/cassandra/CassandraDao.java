package com.diplom.repository.cassandra;


import com.datastax.driver.core.*;
import com.datastax.oss.driver.api.querybuilder.QueryBuilder;
import com.diplom.model.DB;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


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
        session.execute(queryDynamicBuilder);
        session.getState();
    }

    @Override
    public DBResponse fillTable(List<String> entities) {
        entities.forEach(entity -> session.execute(QueryBuilder.insertInto(KEY_SPACE, TABLE).json(entity).build().getQuery()));
        return  null;
    }

    @Override
    public String getDataBase() {
        return DB.APACHE_CASSANDRA.getDb();
    }


    private String createTableQueryDynamicBuilder(List<RequestModel.Column> columns) {
        StringBuilder builder = new StringBuilder("CREATE TABLE IF NOT EXISTS test_table (");
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
