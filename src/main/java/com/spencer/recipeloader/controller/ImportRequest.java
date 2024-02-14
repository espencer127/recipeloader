package com.spencer.recipeloader.controller;

import com.spencer.recipeloader.retrieval.model.recipeml.ImageInfo;

import lombok.Data;

@Data
public class ImportRequest {
    private String filePath;
    private ImageInfo imgInfo;
}
