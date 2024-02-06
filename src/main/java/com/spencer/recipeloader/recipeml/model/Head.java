package com.spencer.recipeloader.recipeml.model;

import lombok.Data;

@Data
public class Head {
    private String title;
    private Categories categories;
    private String yield;
}
