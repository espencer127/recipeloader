package com.spencer.recipeloader.scraper.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipeml.model.Ing;
import com.spencer.recipeloader.scraper.service.ScraperService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AllRecipesRecipeTests {

    @Test
    public void shouldCreateIngArrayCorrectly() {
        String ing1 = "0.5 cup ketchup";
        String ing2 = "2.6666667461395 tablespoons honey";
        String ing3 = "2 tablespoons low-sodium soy sauce";
        String ing4 = "2 cloves garlic, crushed";
        String ing5 = "6 (4 ounce) (1-inch thick) pork chops";

        String[] strArray = {ing1, ing2, ing3, ing4, ing5};

        AllRecipesRecipe rec = new AllRecipesRecipe();
        rec.setRecipeIngredient(strArray);

        Ing[] result = rec.createIngredients();

        log.debug("The result is {}", result);
    }

    @Test
    public void recipeWithSquareBrackets_shouldParseCorrectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RecipeMapper recMapper = Mappers.getMapper(RecipeMapper.class);

        ScraperService scraperService = new ScraperService(recMapper);

        File squareFile = new File("src\\test\\resources\\mockobjects\\allrecipes\\squarebracketrecipe.json");

        String squareRecipe = FileUtils.readFileToString(squareFile, "UTF-8");
        String cleansedRecipe = scraperService.cleanseRecipe(squareRecipe);
        mapper.readValue(cleansedRecipe, AllRecipesRecipe.class);
    }

    @Test
    public void recipeWithoutSquareBrackets_shouldParseCorrectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RecipeMapper recMapper = Mappers.getMapper(RecipeMapper.class);

        ScraperService scraperService = new ScraperService(recMapper);

        File squareFile = new File("src\\test\\resources\\mockobjects\\allrecipes\\recipe.json");

        String squareRecipe = FileUtils.readFileToString(squareFile, "UTF-8");
        String cleansedRecipe = scraperService.cleanseRecipe(squareRecipe);
        mapper.readValue(cleansedRecipe, AllRecipesRecipe.class);
    }
}
