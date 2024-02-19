package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Ingredients {
    
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private Ing[] ing;

    public Ingredients fromPythonRecipe(String[] ings) {

        Ing[] result = new Ing().createIngredients(ings);
        Ingredients ingreds = new Ingredients();
        ingreds.setIng(result);

        return ingreds;
    }

}
