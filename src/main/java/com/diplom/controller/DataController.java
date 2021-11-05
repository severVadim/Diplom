package com.diplom.controller;


import com.diplom.model.RequestModel;
import com.diplom.model.ResponseMetrics;
import com.diplom.repository.RepositoryServiceExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DataController {

    private final RepositoryServiceExecutor repositoryServiceExecutor;

    @PostMapping(value = "/generateEntity")
    public ResponseMetrics generateAssets(@RequestParam(name = "amount", defaultValue = "1000") long amount, @RequestBody List<RequestModel> requestModels) {
        return repositoryServiceExecutor.executeCreation(amount, requestModels);
    }
}
