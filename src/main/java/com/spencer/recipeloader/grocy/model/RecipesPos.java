package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RecipesPos {
    private Integer id;
    private Integer recipe_id;
    private Integer product_id;
    private Integer amount;
    private Integer qu_id;
    private String ingredient_group;
}
