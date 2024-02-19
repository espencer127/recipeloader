package com.spencer.recipeloader.controller;

import com.spencer.recipeloader.universal.model.ImageInfo;

import lombok.Data;

@Data
public class UpdateRequest {
    private ImageInfo imgInfo;
}
