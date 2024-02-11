package com.spencer.recipeloader.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.scraper.model.AllRecipesRecipe;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
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
    //TODO: This shouldn't be hardcoded, we should actually get the locations and throw whichever is the value for 'fridge'
    @Mapping(target = "location_id", constant="2")
    @Mapping(source = "qtyId", target = "qu_id_purchase")
    @Mapping(source = "qtyId", target = "qu_id_stock")
    Product toProductPostBody(String productName, Integer qtyId);

    /**
     * Only mandatory input in this POST body is name, but 
     * let's do plural_name too to be nice
     * @param quantityName
     * @return
     */
    @Mapping(source = "quantityName", target="name")
    @Mapping(source = "quantityName", target="name_plural")
    QuantityUnit toQuantityUnitPostBody(String quantityName);

    RecipesPos toRecipePosPostBodyWithAmount(Integer product_id, Integer recipe_id, Integer amount, Integer qu_id);

    RecipesPos toRecipePosPostBodyWithVariableAmount(Integer product_id, Integer recipe_id, Integer amount, String variable_amount, Integer qu_id);


    //TODO: Keep going here sunday
     //@Mapping(expression= "java(StringUtils.join(Arrays.asList(recipeInstructions).stream().map(x -> x.getText()).collect(Collectors.toList()), \"<br>\"))", target="directions.step")
    @Mapping(expression="java(allRecipesRecipe.createYield())", target="head.yield")
    @Mapping(expression="java(allRecipesRecipe.createIngredients())", target="ingredients.ing")
    @Mapping(expression= "java(allRecipesRecipe.getInstructions())", target="directions.step")
    RecipeDto toRecipeDto(AllRecipesRecipe allRecipesRecipe);


}
