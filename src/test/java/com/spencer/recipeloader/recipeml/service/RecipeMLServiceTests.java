package com.spencer.recipeloader.recipeml.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.spencer.recipeloader.NullChecker;
import com.spencer.recipeloader.recipeml.model.RecipeMLWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RecipeMLServiceTests {

    private RecipeMLService recipeMLService;

    @BeforeEach
    void setup() {
        recipeMLService = new RecipeMLService("src\\test\\resources\\BrowniesRecipe.xml");
    }

    @Test
    public void retrieveRecipeTest_ShouldLoadFile() {
        RecipeMLWrapper rec = recipeMLService.retrieveRecipe();

        log.info("Test class got the recipe {}", rec);

        Boolean result = NullChecker.anyNull(rec);

        assertFalse(result);

    }

}
