package com.diplom.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Operation {
    CREATING ("Create Entity"),
    RETRIEVING("Retrieve data");
    private String operation;
}
