package com.spencer.recipeloader.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.retrieval.ScraperServiceImpl;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;

@RestController
public class ApiController {

    ScraperServiceImpl scraperService;
    FileRetrieverServiceImpl fileRetrieverService;
    GrocyService grocyService;

    public ApiController(ScraperServiceImpl scraperService, FileRetrieverServiceImpl fileRetrieverService, GrocyService grocyService) {
        this.scraperService = scraperService;
        this.fileRetrieverService = fileRetrieverService;
        this.grocyService = grocyService;
    }

    @PostMapping(value = "/scrape", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto scrape(@RequestBody ScrapeRequest requestBody) throws JsonProcessingException {
        RecipeDto result = scraperService.retrieveRecipe(requestBody);
        return result;
    }

    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto importFile(@RequestBody ImportRequest requestBody) throws JsonProcessingException {
        RecipeDto result = fileRetrieverService.retrieveRecipe(requestBody);
        return result;
    }

    @PostMapping("/insert")
    public RecipeDto insert(@RequestBody RecipeDto recipe) {
        grocyService.sendInfoToGrocy(recipe);
        return recipe;
    }
}
