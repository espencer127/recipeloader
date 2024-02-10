package com.spencer.recipeloader.grocy.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
public class Product {
    private Integer id;
    private String name;
    private String description;
    private Integer location_id;
    private Integer qu_id_purchase;
    private Integer qu_id_stock;
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
