package com.diplom.component;

import com.diplom.model.EntityModel;
import com.diplom.model.api.RequestModel;
import com.diplom.util.EntityBuilder;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class EntityGenerator {

    @SneakyThrows
    public List<List<EntityModel>> generateEntities(long amount, List<RequestModel> requestModels) {
        List<List<EntityModel>> entities = new ArrayList<>();
        for (long i = 1; i <= amount; i++) {
            entities.add(EntityBuilder.buildEntity(requestModels));
        }
        return entities;
    }
}


