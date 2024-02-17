package com.spencer.recipeloader.utils;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class Utils {

    
    /**
     * For example, if a list contains both "teaspoon" and "teaspoons", it'll remove
     * the "teaspoons"
     * 
     * @param neededItems
     */
    public static List<String> removePlurals(List<String> neededItems) {
        neededItems.removeIf(x -> neededItems.stream().anyMatch(y -> StringUtils.equals(y, StringUtils.chop(x))));

        neededItems.removeIf(x -> neededItems.stream().anyMatch(y -> StringUtils.equals(x, StringUtils.chop(y))));

        return neededItems;
    }

    /**
     * Adds an 's' to the end of a word.
     * 
     * @param word
     * @return word + s
     */
    public static String madePlural(String word) {
        String pluralWord = word + "s";
        return pluralWord;
    }

}
