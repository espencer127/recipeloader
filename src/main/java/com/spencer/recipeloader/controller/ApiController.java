package com.spencer.recipeloader.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.retrieval.ScraperServiceImpl;
import com.spencer.recipeloader.retrieval.image.ImageRetriever;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.scraper.ImageInfo;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class ApiController {

    ScraperServiceImpl scraperService;
    FileRetrieverServiceImpl fileRetrieverService;
    ImageRetriever imageRetriever;
    GrocyService grocyService;

    public ApiController(ScraperServiceImpl scraperService, FileRetrieverServiceImpl fileRetrieverService, ImageRetriever imageRetriever, GrocyService grocyService) {
        this.scraperService = scraperService;
        this.fileRetrieverService = fileRetrieverService;
        this.imageRetriever = imageRetriever;
        this.grocyService = grocyService;
    }

    @PostMapping(value = "/scrape", produces = MediaType.APPLICATION_JSON_VALUE)
    public FullResponse scrape(@RequestBody ScrapeRequest requestBody) throws JsonProcessingException {
        RecipeDto result = scraperService.retrieveRecipe(requestBody);
        ImageInfo imgResult = imageRetriever.downloadImage(requestBody.getURL());

        FullResponse resp = new FullResponse();
        resp.setRecipe(result);
        resp.setImage(imgResult);

        return resp;
    }

    @PostMapping(value = "/import", produces = MediaType.APPLICATION_JSON_VALUE)
    public RecipeDto importFile(@RequestBody ImportRequest requestBody) throws JsonProcessingException {
        RecipeDto result = fileRetrieverService.retrieveRecipe(requestBody);
        return result;
    }

    @PostMapping("/insert")
    public FullResponse insert(@RequestBody FullResponse recipe) {
        log.info("Received Insert request for {}", recipe.getRecipe().getHead().getTitle());
        Integer result = grocyService.sendInfoToGrocy(recipe);
        recipe.setRecipeID(result);
        return recipe;
    }
    
    @PostMapping("/insert/image")
    public InsertImageRequest uploadImage(@RequestBody InsertImageRequest imgRequest) {
        log.info("Received Image upload `1 request for {}", imgRequest);
        //not gonna work how i want
        grocyService.downloadThenUploadThenAssociate(imgRequest);
        
        return imgRequest;
    }

    @PostMapping("/update/{entity}/{objectId}")
    public String update(@RequestBody String updateReq, @PathVariable("entity") String entity, @PathVariable("objectId") Integer objectId) {
        log.info("Received update request for {}", updateReq);

        grocyService.updateGrocyObject(updateReq, entity, objectId);
        return updateReq;
    }


}
