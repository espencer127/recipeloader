package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QuantityUnit {
    private Integer id;
    private String name;
    private String description;
    private String name_plural;
    private Integer active;
}
