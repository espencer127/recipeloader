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

    
    // FIXME: need to be able to handle strings like 1 1/2
    /**
     * Returns a whole number for the inputted ratio or decimal, rounded up.
     * 
     * @param ratio
     * @return
     * @throws Exception
     */
    public static Integer parseQty(String ratio) throws Exception {
        Integer returnValue = 0;
        Integer nominator = 0;
        Integer denominator = 0;
        // if there's a space, split out the bit before it
        Integer whole = 0;

        if (StringUtils.equals(ratio, "")) {
            return 1;
        }

        if (ratio.contains(" ")) {
            String[] theParts = StringUtils.split(ratio);

            if (theParts.length != 2) {
                throw new Exception("can't handle the qty " + ratio);
            }

            whole = Integer.valueOf(theParts[0]);
            ratio = theParts[1];
        }

        if (ratio.contains("/")) {
            String[] rat = ratio.split("/");
            nominator = Integer.parseInt(rat[0]);
            denominator = Integer.parseInt(rat[1]);

            if ((whole != 0) && (denominator != 0)) {
                Integer addlValue = whole * denominator;
                nominator = nominator + addlValue;
            }

            returnValue = Integer.parseInt(Long.toString(Math.round(Math.ceil((nominator / denominator)))));
        } else {
            returnValue = Integer.parseInt(Long.toString(Math.round(Math.ceil(Double.parseDouble(ratio)))));
        }

        return returnValue;
    }

}
