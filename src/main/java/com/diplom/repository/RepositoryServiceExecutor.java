package com.diplom.repository;

import com.diplom.component.EntityGenerator;
import com.diplom.model.*;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.model.api.RequestPayload;
import com.diplom.model.api.ResponseMetrics;
import com.diplom.repository.cassandra.CassandraDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RepositoryServiceExecutor {

    private final EntityGenerator entityGenerator;

    private static Map<String, RepositoryService> services = new HashMap<>();

    @Autowired
    private RepositoryServiceExecutor(List<RepositoryService> servicesToLoad, EntityGenerator entityGenerator, CassandraDao cassandraDao) {
        this.entityGenerator = entityGenerator;
        this.services = servicesToLoad.stream().collect(Collectors.toMap(RepositoryService::getDataBase, Function.identity()));
    }

    public ResponseMetrics executeCreation(long amount, RequestPayload requestPayload) {
        List<RepositoryService> servicesToUse = requestPayload.getDatabases().stream().map(db -> services.get(db)).collect(Collectors.toList());
        servicesToUse.forEach(services -> services.createTable(getColumns(requestPayload.getRequestModels())));
        List<String> entities = entityGenerator.generateEntities(amount,  requestPayload.getRequestModels());
        List<DBResponse> responses = servicesToUse.stream().map(services -> services.fillTable(entities)).collect(Collectors.toList());
        return ResponseMetrics.builder().operation(Operation.CREATING.getOperation()).response(responses).build();
    }


    private List<RequestModel.Column> getColumns(List<RequestModel> requestModels){
        return requestModels.stream().map(RequestModel::getColumn).collect(Collectors.toList());
    }
}
