package com.spencer.recipeloader.grocy.model;

import java.time.LocalDateTime;
import java.util.HashMap;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Recipe {
    private Integer id;
    private String name;
    private String description;
    private LocalDateTime row_created_timestamp;
    private String picture_file_name;
    private Integer base_servings;
    private Integer desired_servings;
    private Integer not_check_shoppinglist;
    private String type;
    private String product_id;
    private HashMap<String, String>[] userfields;
}
