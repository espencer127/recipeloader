package com.spencer.recipeloader.controller;

import com.spencer.recipeloader.retrieval.model.scraper.ImageInfo;

import lombok.Data;

@Data
public class UpdateRequest {
    //private Head head;
    //private Ingredients ingredients;
    //private Directions directions;
    private ImageInfo imgInfo;
}
