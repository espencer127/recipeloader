package com.spencer.recipeloader.retrieval;

import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;

public interface RecipeRetrieverService<T> {

    public RecipeDto retrieveRecipe(T input);
}
