package com.spencer.recipeloader.retrieval.model.recipeml;

import lombok.Data;

@Data
public class RecipeDto {
    private Head head;
    private Ingredients ingredients;
    private Directions directions;
}
