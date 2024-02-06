package com.spencer.recipeloader.grocy.service;

public enum Entity {
    PRODUCTS("products"),
    QUANTITY_UNITS("quantity_units"),
    RECIPES("recipes"),
    RECIPES_POS("recipes_pos");

    public final String label;

    private Entity(String label) {
        this.label = label;
    }
}
