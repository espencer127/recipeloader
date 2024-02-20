package com.spencer.recipeloader.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spencer.recipeloader.grocy.model.AllGrocyData;
import com.spencer.recipeloader.grocy.model.RecipeList;
import com.spencer.recipeloader.grocy.service.GrocyClient;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.recipe.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.recipe.retrieval.PythonScraperServiceImpl;
import com.spencer.recipeloader.universal.model.FullResponse;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ApiController {

    FileRetrieverServiceImpl fileRetrieverService;
    PythonScraperServiceImpl scraperService;
    GrocyService grocyService;
    GrocyClient grocyClient;

    public ApiController(FileRetrieverServiceImpl fileRetrieverService, PythonScraperServiceImpl scraperService, GrocyService grocyService, GrocyClient grocyClient) {
        this.fileRetrieverService = fileRetrieverService;
        this.scraperService = scraperService;
        this.grocyService = grocyService;
        this.grocyClient = grocyClient;
    }

    @GetMapping(value = "/get", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeList getRecipes() {
        return new RecipeList().associate(new AllGrocyData(grocyClient).buildFromGrocyData());
    }

    @PostMapping(value = "/scrape", produces = MediaType.APPLICATION_JSON_VALUE)
    public FullResponse scrape(@RequestBody ScrapeRequest requestBody) throws JsonProcessingException {
        return new FullResponse(scraperService).buildFromScrape(requestBody);
    }

    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public FullResponse importFile(@RequestBody ImportRequest requestBody) throws JsonProcessingException {
        return new FullResponse(scraperService).buildFromImport(requestBody);
    }

    @PostMapping("/insert")
    public FullResponse insert(@RequestBody FullResponse recipeAndImage) {
        log.info("Received Insert request for {}", recipeAndImage.getRecipe().getTitle());
        return grocyService.sendInfoToGrocy(recipeAndImage);
    }


    @PostMapping("/update/{entity}/{objectId}")
    public String update(@RequestBody String updateReq, @PathVariable("entity") String entity, @PathVariable("objectId") Integer objectId) {
        log.info("Received update request for {}", updateReq);

        grocyService.updateGrocyObject(updateReq, entity, objectId);
        return updateReq;
    }


}
