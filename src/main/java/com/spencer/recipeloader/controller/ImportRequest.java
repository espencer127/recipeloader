package com.spencer.recipeloader.controller;

import com.spencer.recipeloader.universal.model.ImageInfo;

import lombok.Data;

@Data
public class ImportRequest {
    private String filePath;
    private ImageInfo imgInfo;
}
