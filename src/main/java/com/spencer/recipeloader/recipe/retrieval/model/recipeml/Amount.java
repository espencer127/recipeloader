package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Amount {
    private String qty;
    private String unit;
}
