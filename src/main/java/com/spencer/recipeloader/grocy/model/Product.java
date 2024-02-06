package com.spencer.recipeloader.grocy.model;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Product {
    private Integer id;
    private String name;
    private String description;
    private String product_group_id;
    private Integer active;
    private Integer location_id;
    private String shopping_location_id;
    private Integer qu_id_purchase;
    private Integer qu_id_stock;
    private Integer min_stock_amount;
    private Integer default_best_before_days;
    private Integer default_best_before_days_after_open;
    private Integer default_best_before_days_after_freezing;
    private Integer default_best_before_days_after_thawing;
    private String picture_file_name;
    private Integer enable_tare_weight_handling;
    private Integer tare_weight;
    private Integer not_check_stock_fulfillment_for_recipes;
    private String parent_product_id;
    private Integer calories;
    private Integer cumulate_min_stock_amount_of_sub_products;
    private Integer due_type;
    private Integer quick_consume_amount;
    private Integer hide_on_stock_overview;
    private Integer default_stock_label_type;
    private Integer should_not_be_frozen;
    private Integer treat_opened_as_out_of_stock;
    private Integer no_own_stock;
    private String default_consume_location_id;
    private Integer move_on_open;
    private LocalDateTime row_created_timestamp;
    private Integer qu_id_consume;
    private Integer auto_reprint_stock_label;
    private Integer quick_open_amount;
    private Integer qu_id_price;
}
