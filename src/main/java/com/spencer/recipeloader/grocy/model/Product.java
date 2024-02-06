package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String product_group_id;
    private Integer active;
    private Integer default_best_before_days;
    private Integer default_best_before_days_after_open;
    private Integer default_best_before_days_after_freezing;
    private Integer default_best_before_days_after_thawing;
    private Integer due_type;
    private Integer hide_on_stock_overview;
    private Integer default_stock_label_type;
    private String default_consume_location_id;
    private Integer move_on_open;
}
