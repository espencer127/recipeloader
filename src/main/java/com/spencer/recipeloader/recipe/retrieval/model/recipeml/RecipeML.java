package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import com.spencer.recipeloader.universal.model.RecipeInfo;

import lombok.Data;

@Data
public class RecipeML {
    private RecipeInfo recipe;
    private Double version;
}
