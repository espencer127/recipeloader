package com.spencer.recipeloader.recipeml.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Amount {
    private String qty;
    private String unit;
}
