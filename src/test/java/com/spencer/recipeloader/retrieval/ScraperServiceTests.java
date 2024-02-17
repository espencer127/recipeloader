package com.spencer.recipeloader.retrieval;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.controller.ScrapeRequest;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.retrieval.image.ImageRetriever;
import com.spencer.recipeloader.retrieval.model.recipeml.Directions;
import com.spencer.recipeloader.retrieval.model.recipeml.Ing;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.scraper.AllRecipesInstructions;
import com.spencer.recipeloader.retrieval.model.scraper.AllRecipesRecipe;

import lombok.extern.slf4j.Slf4j;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class ScraperServiceTests {

    @Mock
    JSouper jsouper;

    @Mock
    ImageRetriever imageRetriever;

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

    @Disabled("gotta fix after refactor; cleanse recipe method private now")
    @Test
    public void recipeWithSquareBrackets_shouldParseCorrectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RecipeMapper recMapper = Mappers.getMapper(RecipeMapper.class);

        ScraperServiceImpl scraperService = new ScraperServiceImpl(recMapper, jsouper, imageRetriever);

        File squareFile = new File("src\\test\\resources\\mockobjects\\allrecipes\\squarebracketrecipe.json");

        String squareRecipe = FileUtils.readFileToString(squareFile, "UTF-8");
        //String cleansedRecipe = scraperService.cleanseRecipe(squareRecipe);
        //mapper.readValue(cleansedRecipe, AllRecipesRecipe.class);
    }
    
    @Disabled("gotta fix after refactor; cleanse recipe method private now")
    @Test
    public void recipeWithoutSquareBrackets_shouldParseCorrectly() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        RecipeMapper recMapper = Mappers.getMapper(RecipeMapper.class);

        ScraperServiceImpl scraperService = new ScraperServiceImpl(recMapper, jsouper, imageRetriever);

        File squareFile = new File("src\\test\\resources\\mockobjects\\allrecipes\\recipe.json");

        String squareRecipe = FileUtils.readFileToString(squareFile, "UTF-8");
        //String cleansedRecipe = scraperService.cleanseRecipe(squareRecipe);
        //mapper.readValue(cleansedRecipe, AllRecipesRecipe.class);
    }

    @Test
    public void whenCallUrl_shouldMakeObject() throws IOException {

        File inputt = new File("src\\test\\resources\\mockwebpages\\AllRecipesPorkChopsRecipe.html");
        Document doc = Jsoup.parse(inputt, "UTF-8", "http://example.com/");

        RecipeMapper mapper = Mappers.getMapper(RecipeMapper.class);

        ScraperServiceImpl scraperService = new ScraperServiceImpl(mapper, jsouper, imageRetriever);

        when(jsouper.getDoc(anyString())).thenReturn(doc);
       
        ScrapeRequest input = new ScrapeRequest();
        input.setURL("chuggachugga");

        RecipeDto result = scraperService.retrieveRecipe(input);

        log.debug("We got the result {}", result);
        //TODO: should test stuff about the result
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
