package com.spencer.recipeloader.scraper.service;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipeml.model.Directions;
import com.spencer.recipeloader.scraper.model.AllRecipesInstructions;
import com.spencer.recipeloader.scraper.model.AllRecipesRecipe;

public class ScraperServiceTests {

    @Test
    public void whenCallUrl_shouldMakeObject() {

        RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);

        ScraperService scraperService = new ScraperService(mapper);

        scraperService.scrapeAllRecipes("https://www.allrecipes.com/recipe/235158/worlds-best-honey-garlic-pork-chops/");
        //scraperService.scrapeHungryRoot("https://www.hungryroot.com/products/braised-lemongrass-tofu-nuggets-202/");


    }

    @Test
    public void newBootGoofin() {
        AllRecipesInstructions instOne =
        new AllRecipesInstructions();
        
        instOne.setText("Step 1");

        
        AllRecipesInstructions instTwo =
        new AllRecipesInstructions();
        
        instTwo.setText("Step 2");

        AllRecipesInstructions[] instArray = {instOne, instTwo};
        String[] recipCat = {"dinner", "dessert"};

        AllRecipesRecipe rec = new AllRecipesRecipe();
        rec.setName("test");
        rec.setCookTime("1min");
        rec.setPrepTime("5 min");
        rec.setTotalTime("6 min");
        rec.setRecipeCategory(recipCat);
        rec.setRecipeInstructions(instArray);

        //RecipeDto recipDto = new RecipeDto();
        Directions directions = new Directions();
        directions.setStep(rec.getInstructions());
    }

}
