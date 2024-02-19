package com.spencer.recipeloader.retrieval;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.spencer.recipeloader.recipe.retrieval.PythonScraperServiceImpl;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PythonScraperServiceTests {

    private PythonScraperServiceImpl pythonScraperService;

    @Test
    public void givenPythonScript_whenPythonProcessExecuted_thenSuccess() throws IOException {

        this.pythonScraperService = new PythonScraperServiceImpl();
        
        pythonScraperService.retrieveRecipe("https://www.allrecipes.com/recipe/270944/instant-pot-honey-garlic-chicken/");


    }

}
