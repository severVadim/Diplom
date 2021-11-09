package com.diplom.component;

import com.diplom.model.api.RequestModel;
import com.diplom.util.RandomValueGenerator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class StatementGenerator {

    @SneakyThrows
    public List<String> generateStatements(List<RequestModel> requestModels) {
        List<String> statements = new ArrayList<>();
        String where = "WHERE %s%s%s allow filtering;";

        requestModels.forEach(model -> {
            RequestModel.ValueRange valueRange = model.getValueRange();
            String name = model.getColumn().getName();
            switch (model.getColumn().getType()) {
                case TEXT:
                    statements.add(String.format(where, name, "=",
                            addSingleQuotes(RandomValueGenerator.getRandomString(valueRange.getValues()))));
                    break;
                case BOOLEAN:
                    statements.add(String.format(where, name, "=", RandomValueGenerator.getRandomBoolean()));
                    break;
                case INT:
                case LONG:
                    statements.add(String.format(where, name, ">" ,
                            RandomValueGenerator.getRandomLong(valueRange.getFromValue(), valueRange.getToValue())));
                    break;
                case FLOAT:
                    statements.add(String.format(where, name, ">" ,
                            RandomValueGenerator.getRandomFloat(valueRange.getFromValue(), valueRange.getToValue())));
                    break;
                case DATE:
                     statements.add(String.format(where, name, ">" ,
                             addSingleQuotes(RandomValueGenerator.getRandomDate(valueRange.getFromValue(), valueRange.getToValue()))));
                    break;
                default:
                    throw new RuntimeException();
            }
        });
        return statements;
    }

    private String addSingleQuotes(String value){
        return String.format("'%s'", value);
    }
}


