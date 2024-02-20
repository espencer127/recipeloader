package com.spencer.recipeloader.universal.model;

import java.util.StringJoiner;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.spencer.recipeloader.recipe.retrieval.model.pythonscraper.PythonRecipe;
import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Ingredients;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class RecipeInfo {
    private String title;
    private String categories;
    private String diet;
    private String protein;
    private String numIngredients;
    private String yield;
    private String prepTime;
    private String cookTime;
    private String totalTime;
    private Ingredients ingredients;
    private String directions;
    private String pictureUrl;

    public RecipeInfo buildFromPythonRecipe(PythonRecipe pRec) {

        StringJoiner directionsJoiner = new StringJoiner(" <br> ");
        directionsJoiner.add(pRec.getDescription());
        directionsJoiner.add(pRec.getInstructions());
        directionsJoiner.add(pRec.getCanonical_url());

        RecipeInfo rec = new RecipeInfo();
        rec.setTitle(pRec.getTitle());
        rec.setCategories(pRec.getCategory());
        rec.setYield(pRec.getYields());
        rec.setPrepTime(String.valueOf(pRec.getPrep_time()));
        rec.setCookTime(String.valueOf(pRec.getCook_time()));
        rec.setTotalTime(String.valueOf(pRec.getTotal_time()));
        rec.setIngredients(new Ingredients().fromPythonRecipe(pRec.getIngredients()));
        rec.setDirections(directionsJoiner.toString());

        return rec;
    }
}
