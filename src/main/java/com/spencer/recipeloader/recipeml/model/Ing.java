package com.spencer.recipeloader.recipeml.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Ing {
    private Amount amt;
    private String item;
}
