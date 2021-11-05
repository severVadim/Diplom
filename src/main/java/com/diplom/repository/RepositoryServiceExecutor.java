package com.diplom.repository;

import com.diplom.component.EntityGenerator;
import com.diplom.model.DBResponse;
import com.diplom.model.Operation;
import com.diplom.model.RequestModel;
import com.diplom.model.ResponseMetrics;
import com.diplom.model.cassandra.Column;
import com.diplom.repository.cassandra.CassandraDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepositoryServiceExecutor {

    private final EntityGenerator entityGenerator;

    private static final List<RepositoryService> services = new ArrayList<>();

    @Autowired
    private RepositoryServiceExecutor(List<RepositoryService> servicesToLoad, EntityGenerator entityGenerator, CassandraDao cassandraDao) {
        this.entityGenerator = entityGenerator;
        services.addAll(servicesToLoad);
    }

    public ResponseMetrics executeCreation(long amount, List<RequestModel> requestModels) {
        services.forEach(services -> services.createTable(getColumns(requestModels)));
        List<String> entities = entityGenerator.generateEntities(amount,  requestModels);
        List<DBResponse> responses = services.stream().map(services -> services.fillTable(entities)).collect(Collectors.toList());
        return ResponseMetrics.builder().operation(Operation.CREATING.getOperation()).response(responses).build();
    }


    private List<Column> getColumns(List<RequestModel> requestModels){
        return requestModels.stream().map(RequestModel::getColumn).collect(Collectors.toList());
    }
}
