package com.spencer.recipeloader.retrieval.model.scraper;

import lombok.Data;

@Data
public class ImageInfo {
    private String fileName;
    private String base64FileName;
    private String url;
    private String localPath;
}
