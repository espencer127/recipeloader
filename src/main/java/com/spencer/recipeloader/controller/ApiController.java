package com.spencer.recipeloader.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.scraper.service.ScraperService;

@RestController
public class ApiController {

    ScraperService scraperService;
    GrocyService grocyService;

    public ApiController(ScraperService scraperService, GrocyService grocyService) {
        this.scraperService = scraperService;
        this.grocyService = grocyService;
    }

    @PostMapping(value = "/scrape", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto scrape(@RequestBody ScrapeRequest requestBody) throws JsonProcessingException {
        RecipeDto result = scraperService.scrapeAllRecipes(requestBody.getURL());
        return result;
    }

    @PostMapping("/insert")
    public RecipeDto insert(@RequestBody RecipeDto recipe) {
        grocyService.parseDtoAndSendToGrocy(recipe);
        return recipe;
    }
}
