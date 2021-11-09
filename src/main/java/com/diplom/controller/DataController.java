package com.diplom.controller;


import com.diplom.model.*;
import com.diplom.model.api.ConfigurationModel;
import com.diplom.model.api.RequestPayload;
import com.diplom.model.api.ResponseMetrics;
import com.diplom.service.RepositoryServiceExecutor;
import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class DataController {

    private final RepositoryServiceExecutor repositoryServiceExecutor;

    @PostMapping(value = "/generateEntity")
    public List<ResponseMetrics> generateAssets(@RequestParam(name = "amount", defaultValue = "1000") long amount, @RequestBody RequestPayload requestPayload) {
        return repositoryServiceExecutor.executeQueries(amount, requestPayload);
    }

    @GetMapping(value = "/configuration")
    public ConfigurationModel getConfiguration() {
        return ConfigurationModel.builder().databases(DB.getDBModels()).types(Type.getTypesNames()).build();
    }
}
