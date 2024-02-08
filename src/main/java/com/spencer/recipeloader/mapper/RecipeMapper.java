package com.spencer.recipeloader.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.recipeml.model.RecipeDto;

@Mapper(componentModel = "spring")
public interface RecipeMapper {

    @Mapping(source = "head.title", target="name")
    @Mapping(source = "directions.step", target = "description")
    Recipe toRecipe(RecipeDto recipeDto);

    /** convert string to products post body
     * The minimal POST body looks like this, these fields need to be populated;
     * 
     * {
     * "name": "choc chip Cookies",
     * "location_id": "4",
     * "qu_id_purchase": "3",
     * "qu_id_stock": "3"
     * }
     * @param productInput
     * @return
     */
    @Mapping(source = "productName", target = "name")
    @Mapping(target = "location_id", constant="1")
    @Mapping(target = "qu_id_purchase", constant="1")
    @Mapping(target = "qu_id_stock", constant="1")
    Product toProductPostBody(String productName);

    /**
     * Only mandatory input in this POST body is name, but 
     * let's do plural_name too to be nice
     * @param quantityName
     * @return
     */
    @Mapping(source = "quantityName", target="name")
    @Mapping(source = "quantityName", target="name_plural")
    QuantityUnit toQuantityUnitPostBody(String quantityName);

}
