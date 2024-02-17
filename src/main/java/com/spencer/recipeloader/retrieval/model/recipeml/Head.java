package com.spencer.recipeloader.retrieval.model.recipeml;

import lombok.Data;

@Data
public class Head {
    private String title;
    //private Categories categories;
    private String yield;
    private Time time;
}
