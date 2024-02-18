package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class Categories {
    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private String[] cat;

    public String createCategories() {
        return StringUtils.join(cat, ",");
    }
}
