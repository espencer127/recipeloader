package com.spencer.recipeloader.universal.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.spencer.recipeloader.controller.ImportRequest;
import com.spencer.recipeloader.controller.ScrapeRequest;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipe.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.recipe.retrieval.PythonScraperServiceImpl;
import com.spencer.recipeloader.recipe.retrieval.model.pythonscraper.PythonRecipe;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class FullResponse {
    private RecipeInfo recipe;
    private ImageInfo image;
    private Integer recipeID;

    private PythonScraperServiceImpl scraperService;

    private FileRetrieverServiceImpl fileRetrieverService;

    private RecipeMapper recipeMapper;

    public FullResponse(RecipeInfo recipe, ImageInfo image, Integer recipeID) {
        this.recipe = recipe;
        this.image = image;
        this.recipeID = recipeID;
    }

    public FullResponse(PythonScraperServiceImpl scraperService) {
        this.scraperService = scraperService;
    }

    public FullResponse buildFromScrape(ScrapeRequest requestBody) {
        return scraperService.retrieveRecipe(requestBody.getURL());
    }
    
    public FullResponse buildFromImport(ImportRequest requestBody) {
        return fileRetrieverService.retrieveRecipe(requestBody);
    }

    public FullResponse buildFromPythonRecipe(PythonRecipe recipe2) {
        return FullResponse.builder()
        .recipe(new RecipeInfo().buildFromPythonRecipe(recipe2))
        .image(new ImageInfo().buildFromPythonRecipe(recipe2))
        .build();
    }
}
