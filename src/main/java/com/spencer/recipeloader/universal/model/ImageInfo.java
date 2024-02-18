package com.spencer.recipeloader.universal.model;


import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.spencer.recipeloader.image.retrieval.ImageRetriever;
import com.spencer.recipeloader.recipe.retrieval.model.pythonscraper.PythonRecipe;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Slf4j
public class ImageInfo {
    private String fileName;
    private String base64FileName;
    private String url;
    private String localPath;

    public ImageInfo(String fileName, String base64FileName, String url, String localPath) {
        this.fileName = fileName;
        this.base64FileName = base64FileName;
        this.url = url;
        this.localPath = localPath;
    }

    public ImageInfo buildFromPythonRecipe(PythonRecipe pRec) {
        ImageRetriever imageRetriever = new ImageRetriever();
        return imageRetriever.downloadImage(pRec.getImage());

    }
}
