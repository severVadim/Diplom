package com.diplom.component;

import com.diplom.model.api.RequestModel;
import com.diplom.util.JsonBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityGenerator {

    @SneakyThrows
    public List<String> generateEntities(long amount, List<RequestModel> requestModels) {
        List<String> entities = new ArrayList<>();
        for (long i = 1; i <= amount; i++) {
            entities.add(JsonBuilder.buildJson(requestModels));
        }
        return entities;
    }
}


