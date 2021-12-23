package com.diplom.model;

import com.diplom.model.api.RequestModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatementModel {
    private RequestModel requestModel;
    private Object value;
    private StatementExpresion statementExpresion;
}
