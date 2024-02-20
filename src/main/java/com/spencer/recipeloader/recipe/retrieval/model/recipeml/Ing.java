package com.spencer.recipeloader.recipe.retrieval.model.recipeml;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Ing {
    private Amount amt;
    private String item;

    /**
     * Converts a string array into an Ing array
     * @param scrapedIngredients
     * @return
     */

    public Ing[] createIngredients(String[] scrapedIngredients) {
        List<Ing> ingList = new ArrayList<>();

        for (String recipIng : scrapedIngredients) {
            String unitAndItem = StringUtils.trim(recipIng.replaceAll("[.0123456789]",""));
            String unit = StringUtils.substringBefore(unitAndItem, " ");
            String item = StringUtils.substringAfter(unitAndItem, " ");
            String qty = StringUtils.trim(recipIng.replaceAll("[^\\d.]", ""));

            Amount amt = new Amount();
            amt.setQty(qty);
            amt.setUnit(unit);
            Ing ing = new Ing();
            ing.setAmt(amt);
            ing.setItem(item);

            ingList.add(ing);
        }

        Ing[] result = ingList.stream().toArray(Ing[] ::new);

        return result;
    }
}
