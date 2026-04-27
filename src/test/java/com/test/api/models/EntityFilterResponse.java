package com.test.api.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EntityFilterResponse {

    private List<EntityResponse> entity;

    private Integer page;

    private Integer perPage;
}