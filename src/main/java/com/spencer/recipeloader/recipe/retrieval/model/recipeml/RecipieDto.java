package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import lombok.Data;

@Data
public class RecipieDto {
    private Head head;
    private Ingredients ingredients;
    private Directions directions;

}
