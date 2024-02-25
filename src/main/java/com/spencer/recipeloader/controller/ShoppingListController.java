package com.spencer.recipeloader.controller;

import java.util.List;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.spencer.recipeloader.universal.model.RecipeInfo;
import com.spencer.recipeloader.universal.model.ShoppingList;

@RestController
public class ShoppingListController {

    @PostMapping("/shoppingList/get")
    public ShoppingList getShoppingList(@RequestBody List<RecipeInfo> recipeInfo) {
        return new ShoppingList(recipeInfo);
    }
}
