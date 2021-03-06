package com.diplom.model.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestPayload {
    List<String> databases;
    List<RequestModel> requestModels;
}
