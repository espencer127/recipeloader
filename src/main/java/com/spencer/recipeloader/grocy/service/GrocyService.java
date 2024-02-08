package com.spencer.recipeloader.grocy.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipeml.model.Ing;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.recipeml.service.RecipeMLService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GrocyService {

    public RecipeMLService recipeMLService;
    public RecipeMapper recipeMapper;
    public GrocyClient grocyClient;

    public GrocyService(RecipeMLService recipeMLService, RecipeMapper recipeMapper, GrocyClient grocyClient) {
        this.recipeMLService = recipeMLService;
        this.recipeMapper = recipeMapper;
        this.grocyClient = grocyClient;
    }

    /**
     * Does the whole shebang:
     * <ul>
     * Read RecipeML file
     * </ul>
     * <li>Convert XML recipe into grocy format POJO object</li>
     * <li>API: GET the user's existing ingredients ("products")</li>
     * <li>API: GET the user's existing quantities ("quantity_units")</li>
     * <li>API: POST make any necessary quantities ("quantity_units")</li>
     * <li>API: POST make any necessary ingredients ("products")</li>
     * <li>(add that ingredient to the internal ingredient list object)</li>
     * <li>API: POST make the recipe ("recipe")</li>
     * <li>API: POST add each ingredient to the recipe ("recipe_pos")</li>
     */
    public void execute() {

        RecipeDto recipeDto = recipeMLService.retrieveRecipe().getRecipeml().getRecipe();
        Recipe recipe = recipeMapper.toRecipe(recipeDto);

        log.debug("got the recipe {}", recipe);

        List<Product> existingUserIngredients = grocyClient.getProducts();
        List<QuantityUnit> existingUserQuantityUnits = grocyClient.getQuantityUnits();

        log.debug("got the existing ingredients {}", existingUserIngredients);
        log.debug("got the existing quantity units: {}", existingUserQuantityUnits);

        List<String> quantitiesWeNeedToAdd = findQuantitiesWeNeedToAdd(recipeDto, existingUserQuantityUnits);
        List<String> ingredientsWeNeedToAdd = findIngredientsWeNeedToAdd(recipeDto, existingUserIngredients);

        // add quantities+products

        if (quantitiesWeNeedToAdd.size() > 0) {
            List<QuantityUnit> addedQuantityUnits = grocyClient.createQuantityUnits(quantitiesWeNeedToAdd);
        }

        if (ingredientsWeNeedToAdd.size() > 0) {
            List<Product> addedProducts = grocyClient.createProducts(ingredientsWeNeedToAdd);
        }

        // TODO: add recipe
    }

    private List<String> findQuantitiesWeNeedToAdd(RecipeDto recipeDto, List<QuantityUnit> existingUserQuantityUnits) {

        List<QuantityUnit> nonNullExistingQUs = existingUserQuantityUnits
                .stream()
                .filter(x -> (!StringUtils.isEmpty(StringUtils.deleteWhitespace(x.getName())))) 
                .collect(Collectors.toList());

        List<String> existingUnits = nonNullExistingQUs
                .stream()
                .map(x -> x.getName().trim().toLowerCase())
                .collect(Collectors.toList());

        List<Ing> ingredients = Arrays.asList(recipeDto.getIngredients().getIng());

        List<String> neededQuantityUnits = ingredients
                .stream().map(x -> x.getAmt().getUnit().trim().toLowerCase())
                .collect(Collectors.toList());

        neededQuantityUnits.removeIf(StringUtils::isEmpty);

        neededQuantityUnits.removeAll(existingUnits);

        List<String> dedupedList = new ArrayList<>(
                new HashSet<>(neededQuantityUnits));

        return dedupedList;
    }

    private List<String> findIngredientsWeNeedToAdd(RecipeDto recipeDto, List<Product> existingUserIngredients) {
        List<String> existingIngredients = existingUserIngredients
                .stream().map(x -> x.getName().trim().toLowerCase())
                .collect(Collectors.toList());

        List<Ing> neededIngredients = Arrays.asList(recipeDto.getIngredients().getIng());

        List<String> neededIngredientStrings = neededIngredients
                .stream().map(x -> x.getItem().trim().toLowerCase())
                .collect(Collectors.toList());

        neededIngredientStrings.removeAll(existingIngredients);

        List<String> dedupedList = new ArrayList<>(
                new HashSet<>(neededIngredientStrings));

        return dedupedList;
    }

}
