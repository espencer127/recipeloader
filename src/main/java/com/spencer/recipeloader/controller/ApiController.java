package com.spencer.recipeloader.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.scraper.service.ScraperService;

@RestController
public class ApiController {

    ScraperService scraperService;

    public ApiController(ScraperService scraperService) {
        this.scraperService = scraperService;
    }

    @PostMapping("/scrape")
    public String scrape(@RequestBody ScrapeRequest requestBody) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        RecipeDto result = scraperService.scrapeAllRecipes(requestBody.getURL());
        return mapper.writeValueAsString(result);
    }
}
