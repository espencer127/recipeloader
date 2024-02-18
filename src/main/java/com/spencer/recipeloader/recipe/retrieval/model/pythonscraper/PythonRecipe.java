package com.spencer.recipeloader.recipe.retrieval.model.pythonscraper;

import lombok.Data;

@Data
public class PythonRecipe {
    private String author;
    private String canonical_url;
    private String category;
    private Integer cook_time;
    private String cuisine;
    private String description;
    private String host;
    private String image;
    private IngredientGroup[] ingredient_groups;
    private String[] ingredients;
    private String instructions;
    private String[] instructions_list;
    private String language;
    private NutritionInfo nutrients;
    private Integer prep_time;
    private Double ratings;
    private String site_name;
    private String title;
    private Integer total_time;
    private String yields;

    @Data
    public static class IngredientGroup {
        private String[] ingredients;
        private String purpose;
    }

    @Data
    public static class NutritionInfo {
        private String calories;
        private String carbohydrateContent;
        private String cholesterolContent;
        private String fiberContent;
        private String proteinContent;
        private String saturatedFatContent;
        private String sodiumContent;
        private String sugarContent;
        private String fatContent;
        private String unsaturatedFatContent;
    }
}
