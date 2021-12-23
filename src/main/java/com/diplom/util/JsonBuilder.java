package com.diplom.util;

import com.diplom.model.Type;
import com.diplom.model.api.RequestModel;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.util.List;

public class JsonBuilder {


    @SneakyThrows
    public static String buildJson(List<RequestModel> requestModels) {
        JSONObject jsonObject = new JSONObject();
        for (RequestModel model : requestModels) {
            jsonObject.put(model.getColumn().getName(), getValue(model));
        }
        return jsonObject.toString();
    }

    private static Object getValue(RequestModel model) {
        RequestModel.ValueRange valueRange = model.getValueRange();
        switch (model.getColumn().getType()) {
            case TEXT:
                return RandomValueGenerator.getRandomString(valueRange.getValues());
            case BOOLEAN:
                return RandomValueGenerator.getRandomBoolean();
            case INT:
            case LONG:
                return RandomValueGenerator.getRandomLong(valueRange.getFromValue(), valueRange.getToValue());
            case FLOAT:
                return RandomValueGenerator.getRandomFloat(valueRange.getFromValue(), valueRange.getToValue());
            case DATE:
                return RandomValueGenerator.getRandomDate(valueRange.getFromValue(), valueRange.getToValue());
            default:
                throw new RuntimeException();
        }
    }

    public static String addSingleQuotes(String value, Type type){
        if (type.equals(Type.DATE) || type.equals(Type.TEXT)){
            return String.format("'%s'", value);
        }
        return value;
    }
}
