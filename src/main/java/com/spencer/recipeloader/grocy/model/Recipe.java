package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Recipe {
    private Integer id;
    private String name;
    private String description;
}
