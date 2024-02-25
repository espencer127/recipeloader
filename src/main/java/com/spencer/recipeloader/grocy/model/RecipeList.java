package com.spencer.recipeloader.grocy.model;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Amount;
import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Ing;
import com.spencer.recipeloader.recipe.retrieval.model.recipeml.Ingredients;
import com.spencer.recipeloader.universal.model.RecipeInfo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RecipeList {
    List<RecipeInfo> recipes;

    public RecipeList(List<RecipeInfo> recList) {
        this.recipes = recList;
    }

    /**
     * Iterate through each of the recipes we got from Grocy. For each one, we got all the Recipe POS's for that
     * recipe. From that, we'll be ble to get the ingred and the qty. We'll concat the ingred + qty together then add
     * to full response ingred list.
     * @return
     */
    public RecipeList associate(AllGrocyData allGrocyData) {
        // TODO Auto-generated method stub

        List<RecipeInfo> recList = new ArrayList<>();
        List<Recipe> allGrocyRecipes = allGrocyData.getRecipes();

        Map<Integer, List<RecipesPos>> recipeToRecipePosMap = new HashMap<>();

        allGrocyRecipes.forEach(x -> {
            Recipe activeRecipe = allGrocyData.getRecipes().stream().filter(z -> z.getId().equals(x.getId())).findFirst().get();
            Integer activeRecipeId = x.getId();
            List<RecipesPos> rpForThisRecipe = allGrocyData.getRecipePoss().stream()
            .filter(y -> y.getRecipe_id().equals(activeRecipeId))
            .collect(Collectors.toList());
            recipeToRecipePosMap.put(activeRecipeId, rpForThisRecipe);

            List<Ing> ingList = new ArrayList<>();

            //for each recipePOS for this recipe, get the product + qty + amt
            rpForThisRecipe.forEach(rp -> {
                QuantityUnit matchingQty = allGrocyData.getQuantityUnits().stream().filter(qtyIt -> qtyIt.getId().equals(rp.getQu_id())).findFirst().get();
                Product matchingProduct = allGrocyData.getProducts().stream().filter(prdIt -> prdIt.getId().equals(rp.getProduct_id())).findFirst().get();
                //TODO: based on if amt is singular or plural, get matchingQty name or plural name
                Amount tempAmt = new Amount(String.valueOf(rp.getAmount()), matchingQty.getName());
                ingList.add(new Ing(tempAmt, matchingProduct.getName(), ""));
            });
            
            Ing[] ingArray = ingList.stream().toArray(Ing[] ::new);

            RecipeInfo recInfo = new RecipeInfo();
            recInfo.setTitle(activeRecipe.getName());
            recInfo.setCategories(activeRecipe.getUserfields().getCategory());
            recInfo.setYield(String.valueOf(activeRecipe.getBase_servings()));
            recInfo.setPrepTime(activeRecipe.getUserfields().getPreptime());
            recInfo.setCookTime(activeRecipe.getUserfields().getCooktime());
            recInfo.setTotalTime(activeRecipe.getUserfields().getTotaltime());
            recInfo.setDiet(activeRecipe.getUserfields().getDiet());
            recInfo.setNumIngredients(activeRecipe.getUserfields().getNumingredients());
            recInfo.setProtein(activeRecipe.getUserfields().getProtein());
            recInfo.setIngredients(new Ingredients(ingArray));
            recInfo.setDirections(activeRecipe.getDescription());

            if (activeRecipe.getPicture_file_name() != null) {
                String pictureURL = "http://localhost:8080/api/files/recipepictures/" + Base64.getEncoder().encodeToString(activeRecipe.getPicture_file_name().getBytes());
                recInfo.setPictureUrl(pictureURL);
            }

            recList.add(recInfo);
        });
        
        return new RecipeList(recList);
    }

}
