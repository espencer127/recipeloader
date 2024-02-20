package com.spencer.recipeloader.grocy.model;

import java.util.List;

import com.spencer.recipeloader.grocy.service.GrocyClient;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Holds all the info necessary to recreate a full recipe saved to grocy
 */
@Data
@NoArgsConstructor
public class AllGrocyData {
    List<Recipe> recipes;
    List<RecipesPos> recipePoss;
    List<Product> products;
    List<QuantityUnit> quantityUnits;

    private GrocyClient grocyClient;

    public AllGrocyData(GrocyClient grocyClient) {
        this.grocyClient = grocyClient;
    }

    public AllGrocyData buildFromGrocyData() {
        AllGrocyData resp = new AllGrocyData();
        resp.setRecipes(grocyClient.getRecipes());
        resp.setProducts(grocyClient.getProducts());
        resp.setRecipePoss(grocyClient.getRecipesPos());
        resp.setQuantityUnits(grocyClient.getQuantityUnits());

        return resp;
    }

}
