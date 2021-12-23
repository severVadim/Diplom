package com.diplom.component.statement;

import com.diplom.model.StatementExpresion;
import com.diplom.model.StatementModel;
import com.diplom.model.api.RequestModel;
import com.diplom.util.RandomValueGenerator;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class StatementGenerator {

     public List<StatementModel> statementValueGenerator(List<RequestModel> requestModels) {
        return requestModels.stream().map(model -> {
            String name = model.getColumn().getName();
            RequestModel.ValueRange valueRange = model.getValueRange();
            switch (model.getColumn().getType()) {
                case TEXT:
                    return new StatementModel(model, RandomValueGenerator.getRandomString(valueRange.getValues()), StatementExpresion.EQUAL);
                case BOOLEAN:
                    return new StatementModel(model,RandomValueGenerator.getRandomBoolean(),StatementExpresion.EQUAL);
                case INT:
                case LONG:
                    return new StatementModel(model,RandomValueGenerator.getRandomLong(valueRange.getFromValue(), valueRange.getToValue()),
                            StatementExpresion.GREATER_THEN);
                case FLOAT:
                     return new StatementModel(model,RandomValueGenerator.getRandomFloat(valueRange.getFromValue(), valueRange.getToValue()),
                             StatementExpresion.GREATER_THEN);
                case DATE:
                    return new StatementModel(model,RandomValueGenerator.getRandomDate(valueRange.getFromValue(), valueRange.getToValue()),
                            StatementExpresion.GREATER_THEN);
                default:
                    throw new RuntimeException();
            }
        }).collect(Collectors.toList());

    }
}
