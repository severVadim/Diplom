package com.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArgumentWrapper {
    private List<String> entities;
    private List<StatementModel> statements;
}
