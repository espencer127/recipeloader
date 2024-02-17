package com.spencer.recipeloader.retrieval.model.scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.spencer.recipeloader.retrieval.model.recipeml.Amount;
import com.spencer.recipeloader.retrieval.model.recipeml.Ing;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class AllRecipesRecipe {
    private String name;
    private String cookTime;
    private String prepTime;
    private String totalTime;
    private String[] recipeYield;
    private String[] recipeCategory;
    private String[] recipeCuisine;
    private String[] recipeIngredient;
    private AllRecipesInstructions[] recipeInstructions;

    public String getInstructions() {
        List<AllRecipesInstructions> instList = Arrays.asList(recipeInstructions);

        List<String> steps = instList.stream().map(x -> x.getText()).collect(Collectors.toList());
        
        return StringUtils.join(steps, "<br>");
    }

    public Ing[] createIngredients() {
        List<Ing> ingList = new ArrayList<>();

        for (String recipIng : recipeIngredient) {
            String unitAndItem = StringUtils.trim(recipIng.replaceAll("[.0123456789]",""));
            String unit = StringUtils.substringBefore(unitAndItem, " ");
            String item = StringUtils.substringAfter(unitAndItem, " ");
            String qty = StringUtils.trim(recipIng.replaceAll("[^\\d.]", ""));

            Amount amt = new Amount();
            amt.setQty(qty);
            amt.setUnit(unit);
            Ing ing = new Ing();
            ing.setAmt(amt);
            ing.setItem(item);

            ingList.add(ing);
        }

        Ing[] result = ingList.stream().toArray(Ing[] ::new);

        return result;
    }

    public String createYield() {
        return StringUtils.join(recipeYield, ",");
    }

    public String createRecipeDescription() {
        throw new UnsupportedOperationException("Unimplemented method 'createRecipeDescription'");
    }

}
