package com.spencer.recipeloader.controller;

import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.scraper.ImageInfo;

import lombok.Data;

@Data
public class FullResponse {
    private RecipeDto recipe;
    private ImageInfo image;
    private Integer recipeID;
}
