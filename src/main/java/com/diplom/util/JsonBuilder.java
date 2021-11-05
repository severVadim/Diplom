package com.diplom.util;

import com.diplom.model.RequestModel;
import lombok.SneakyThrows;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class JsonBuilder {

    public static final Random RANDOM = new Random();

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
                List<Object> values = valueRange.getValues();
                return values.get(RANDOM.nextInt(values.size()));
            case BOOLEAN:
                return RANDOM.nextBoolean();
            case INT:
            case LONG:
                return (long)valueRange.getFromValue() + (long) (Math.random() * (valueRange.getToValue() - valueRange.getFromValue()));
            case FLOAT:
                return valueRange.getFromValue() + RANDOM.nextFloat() * (valueRange.getToValue() - valueRange.getFromValue());
            case DATE:
                return randomDate((long) valueRange.getFromValue(), (long) valueRange.getToValue());
            default:
                throw new RuntimeException();
        }
    }

    public static String randomDate(long from, long to) {
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        long random = ThreadLocalRandom
                .current()
                .nextLong(from, to);
        return df2.format(random);
    }
}
