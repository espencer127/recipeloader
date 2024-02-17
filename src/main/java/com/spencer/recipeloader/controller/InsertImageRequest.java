package com.spencer.recipeloader.controller;

import lombok.Data;

@Data
public class InsertImageRequest {
    Integer recipeId;
    String imageUrl;
}
