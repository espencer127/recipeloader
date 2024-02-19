package com.spencer.recipeloader.recipe.retrieval;

import com.spencer.recipeloader.universal.model.FullResponse;

public interface RecipeRetrieverService<T> {

    public FullResponse retrieveRecipe(T input);
}
