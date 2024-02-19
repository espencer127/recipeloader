package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import lombok.Data;

@Data
public class Head {
    private String title;
    //private Categories categories;
    private String yield;
    private Time time;
}
