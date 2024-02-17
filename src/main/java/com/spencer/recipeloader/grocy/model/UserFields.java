package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class UserFields {
    private String category;
    private String cooktime;
    private String diet;
    private String numingredients;
    private String preptime;
    private String totaltime;
}
