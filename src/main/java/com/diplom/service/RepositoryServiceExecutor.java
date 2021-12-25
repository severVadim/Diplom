package com.diplom.service;

import com.diplom.component.EntityGenerator;
import com.diplom.component.statement.StatementGenerator;
import com.diplom.model.ArgumentWrapper;
import com.diplom.model.EntityModel;
import com.diplom.model.StatementModel;
import com.diplom.model.api.DBResponse;
import com.diplom.model.api.RequestModel;
import com.diplom.model.api.RequestPayload;
import com.diplom.model.api.ResponseMetrics;
import com.diplom.repository.RepositoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class RepositoryServiceExecutor {

    private final EntityGenerator entityGenerator;
    private final StatementGenerator statementGenerator;

    private static Map<String, RepositoryService> services = new HashMap<>();

    private final static List<BiFunction<ArgumentWrapper, List<RepositoryService>, List<ResponseMetrics>>> METHODS = Arrays.asList(
            RepositoryServiceExecutor::executeCreation,
            RepositoryServiceExecutor::executeGetWithLimit,
            RepositoryServiceExecutor::executeWithStatement
    );

    @Autowired
    private RepositoryServiceExecutor(List<RepositoryService> servicesToLoad, EntityGenerator entityGenerator, StatementGenerator statementGenerator) {
        this.entityGenerator = entityGenerator;
        this.services = servicesToLoad.stream().collect(Collectors.toMap(RepositoryService::getDataBase, Function.identity()));
        this.statementGenerator = statementGenerator;
    }

    public List<ResponseMetrics> executeQueries(long amount, RequestPayload requestPayload) {
        List<RepositoryService> servicesToUse = requestPayload.getDatabases().stream().map(db -> services.get(db)).collect(Collectors.toList());
        servicesToUse.forEach(services -> services.createTable(getColumns(requestPayload.getRequestModels())));
        ArgumentWrapper argumentWrapper = new ArgumentWrapper(entityGenerator.generateEntities(amount, requestPayload.getRequestModels()),
                statementGenerator.statementValueGenerator(requestPayload.getRequestModels()));
        List<List<ResponseMetrics>> responseMetrics = METHODS.stream().map(method -> method.apply(argumentWrapper, servicesToUse)).collect(Collectors.toList());
        return responseMetrics.stream().flatMap(Collection::stream).collect(Collectors.toList());
    }

    private static List<ResponseMetrics> executeCreation(ArgumentWrapper argumentWrapper, List<RepositoryService> services) {
        List<List<EntityModel>> entities = argumentWrapper.getEntities();
        List<DBResponse> responses = services.stream().map(service -> service.fillTable(entities)).collect(Collectors.toList());
        return Collections.singletonList(ResponseMetrics.builder().operation(String.format("Create %s entities", entities.size())).response(responses).build());
    }

    private static List<ResponseMetrics> executeGetWithLimit(ArgumentWrapper argumentWrapper, List<RepositoryService> services) {
        List<List<EntityModel>> entities = argumentWrapper.getEntities();
        List<Integer> limits = List.of(1, getPercentage(entities.size(), 5),
                getPercentage(entities.size(), 20),
                getPercentage(entities.size(), 50));
        return limits.stream()
                .map(limit -> ResponseMetrics.builder().operation(String.format("Retrieve %s entities", limit)).response(services
                        .stream()
                        .map(service -> service.getWithLimit(limit))
                        .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private static List<ResponseMetrics> executeWithStatement(ArgumentWrapper argumentWrapper, List<RepositoryService> services) {
        List<StatementModel> statements = argumentWrapper.getStatements();

        return statements.stream()
                .map(statement -> ResponseMetrics.builder()
                        .operation(String.format("Retrieve entities by filed %s with statement - %s %s", statement.getRequestModel().getColumn().getName(),
                                statement.getStatementExpresion().name(), statement.getValue()))
                        .response(services
                                .stream()
                                .map(service -> service.getWithStatement(statement))
                                .collect(Collectors.toList()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<RequestModel.Column> getColumns(List<RequestModel> requestModels) {
        return requestModels.stream().map(RequestModel::getColumn).collect(Collectors.toList());
    }

    private static int getPercentage(int number, int percentage) {
        return number * percentage / 100;
    }
}
