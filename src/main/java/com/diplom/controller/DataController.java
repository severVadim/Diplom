package com.diplom.controller;


import com.diplom.model.*;
import com.diplom.model.api.ConfigurationModel;
import com.diplom.model.api.RequestPayload;
import com.diplom.model.api.ResponseMetrics;
import com.diplom.repository.RepositoryServiceExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class DataController {

    private final RepositoryServiceExecutor repositoryServiceExecutor;

    @PostMapping(value = "/generateEntity")
    public ResponseMetrics generateAssets(@RequestParam(name = "amount", defaultValue = "1000") long amount, @RequestBody RequestPayload requestPayload) {
        return repositoryServiceExecutor.executeCreation(amount, requestPayload);
    }

    @GetMapping(value = "/configuration")
    public ConfigurationModel getConfiguration() {
        return ConfigurationModel.builder().databases(DB.getDBModels()).types(Type.getTypesNames()).build();
    }
}
