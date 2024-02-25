package com.spencer.recipeloader.controller;

import lombok.Data;

@Data
public class ScrapeRequest {
    private String URL;
    private Boolean wildWestMode;
}
