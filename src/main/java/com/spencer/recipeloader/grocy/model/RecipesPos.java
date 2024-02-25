package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class RecipesPos {
    private Integer id;
    private Integer recipe_id;
    private Integer product_id;
    private Integer amount;
    private Integer qu_id;
    private String ingredient_group;
    private String variable_amount;
    private String note;

}
