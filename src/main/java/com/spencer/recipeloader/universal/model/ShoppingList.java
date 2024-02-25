package com.spencer.recipeloader.universal.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Amount;
import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Ing;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShoppingList {
    /**
     * Map<Ingredient Name<Recipe, {Qty, Unit}>>
     */
    public HashMap<String, List<IngredientOccurrence>> ingredientMap;

    /**
     * Figure out the unique ingredient names (uniqueIngredientNames)
     * 
     * create map <String, String[]> which would be <Ing Name, Recipe Instance>
     * For each recipe, for each ing name
     *  matchinging[] = rec.ing[].stream.filter(x -> x.item.equals(ingname))
     *  for each matchinging, stringbuilder.append(qty + amt) (comma delineated)
     * add an entry to the list<recinstance> for that ingname
     * THEN replace it in the map/accumulate it
     * <ing name, ("qty + amt (rec name)")>
     * (maybe won't do that though)
     * @param recipeInfo
     */
    public ShoppingList(List<RecipeInfo> recipeInfo) {
        List<String> uniqueIngredientNames = new ArrayList<>();
        
        for (RecipeInfo rec : recipeInfo) {
            List<Ing> ingList = Arrays.asList(rec.getIngredients().getIng());
            List<String> ingredientNames = ingList.stream().map(ing -> ing.getItem()).collect(Collectors.toList());
            for (String name : ingredientNames) {
                if (!uniqueIngredientNames.contains(name)) {
                    uniqueIngredientNames.add(name);
                }
            }
        }

        HashMap<String, List<IngredientOccurrence>> resultObject = new HashMap<>();

        for (String ingName : uniqueIngredientNames) {
            List<IngredientOccurrence> ingOccurrences = new ArrayList<>();
            for (RecipeInfo reci : recipeInfo) {
                List<Ing> ingList = Arrays.asList(reci.getIngredients().getIng());
                List<Ing> ingInstancesInThisRecipe = ingList.stream().filter(ingre -> ingre.getItem().equals(ingName)).collect(Collectors.toList());

                for (Ing ingInstance : ingInstancesInThisRecipe) {
                    ingOccurrences.add(new IngredientOccurrence(reci.getTitle(), ingInstance.getAmt().getQty(), ingInstance.getAmt().getUnit()));
                }
            }
            //add the List<IngredOccurrence> of ingOccurrences to the map w/ the ingName
            resultObject.put(ingName, ingOccurrences);
        }
        
        this.ingredientMap = resultObject;
    }

    public class IngredientOccurrence {
        public final String recipe;
        public final Amount amt;

        public IngredientOccurrence(String recipe, Amount amt) {
            this.recipe = recipe;
            this.amt = amt;
        }

        public IngredientOccurrence(String recipe, String qty, String unit) {
            this.recipe = recipe;
            this.amt = new Amount(qty, unit);
        }
    }
}
