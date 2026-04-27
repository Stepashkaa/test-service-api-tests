package com.test.api.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class EntityResponse {

    private Integer id;

    private String title;

    private Boolean verified;

    private AdditionResponse addition;

    @JsonProperty("important_numbers")
    private List<Integer> importantNumbers;
}