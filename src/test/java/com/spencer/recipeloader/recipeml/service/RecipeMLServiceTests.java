package com.spencer.recipeloader.recipeml.service;

import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;

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
        recipeMLService = new RecipeMLService();
    }

    @Test
    public void retrieveRecipeTest_ShouldLoadFile() {
        File file = new File("src\\test\\resources\\LemonBars.xml");
        RecipeMLWrapper rec = recipeMLService.retrieveRecipe(file);

        log.info("Test class got the recipe {}", rec);

        Boolean result = NullChecker.anyNull(rec);

        assertFalse(result);

    }

}
