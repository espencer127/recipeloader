package com.spencer.recipeloader.recipeml.model;

import lombok.Data;

@Data
public class Recipe {
    private Head head;
    private Ingredients ingredients;
    private Directions directions;
}
