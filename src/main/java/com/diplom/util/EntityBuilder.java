package com.diplom.util;

import com.diplom.model.EntityModel;
import com.diplom.model.Type;
import com.diplom.model.api.RequestModel;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class EntityBuilder {


    @SneakyThrows
    public static List<EntityModel> buildEntity(List<RequestModel> requestModels) {
        return requestModels.stream().map(EntityBuilder::toEntityModel).collect(Collectors.toList());
    }

    private static EntityModel toEntityModel(RequestModel model) {
        RequestModel.ValueRange valueRange = model.getValueRange();
        EntityModel.EntityModelBuilder entityModelBuilder = EntityModel.builder().filedName(model.getColumn().getName());
        switch (model.getColumn().getType()) {
            case TEXT:
                return entityModelBuilder.value(RandomValueGenerator.getRandomString(valueRange.getValues())).valueType(String.class).build();
            case BOOLEAN:
                return entityModelBuilder.value(RandomValueGenerator.getRandomBoolean()).valueType(Boolean.class).build();
            case INT:
            case LONG:
                return entityModelBuilder.value(RandomValueGenerator.getRandomLong(valueRange.getFromValue(), valueRange.getToValue())).valueType(Long.class).build();
            case FLOAT:
                return entityModelBuilder.value(RandomValueGenerator.getRandomFloat(valueRange.getFromValue(), valueRange.getToValue())).valueType(Float.class).build();
            case DATE:
                return entityModelBuilder.value(RandomValueGenerator.getRandomDate(valueRange.getFromValue(), valueRange.getToValue())).valueType(Date.class).build();
            default:
                throw new RuntimeException();
        }
    }

    public static String addSingleQuotes(String value, Type type) {
        if (type.equals(Type.DATE) || type.equals(Type.TEXT)) {
            return String.format("'%s'", value);
        }
        return value;
    }
}
