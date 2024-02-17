package com.spencer.recipeloader.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.scraper.AllRecipesRecipe;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface RecipeMapper {

    @Mapping(source = "recipeDto.head.title", target="name")
    @Mapping(source = "recipeDto.directions.step", target = "description")
    @Mapping(source = "recipeDto.head.time.prepTime", target="userfields.preptime")
    @Mapping(source = "recipeDto.head.time.cookTime", target="userfields.cooktime")
    @Mapping(source = "recipeDto.head.time.totalTime", target="userfields.totaltime")
    //@Mapping(expression = "java(recipeDto.getHead().getCategories().createCategories())", target="userfields.category")
    Recipe toRecipe(RecipeDto recipeDto, String picture_file_name);

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

    @Mapping(expression="java(allRecipesRecipe.createYield())", target="head.yield")
    //@Mapping(source = "allRecipesRecipe.recipeCategory", target="head.categories.cat")

    @Mapping(expression="java(allRecipesRecipe.createIngredients())", target="ingredients.ing")
    @Mapping(expression= "java(allRecipesRecipe.getInstructions())", target="directions.step")
    @Mapping(source = "allRecipesRecipe.name", target = "head.title")
    @Mapping(source = "allRecipesRecipe.cookTime", target = "head.time.cookTime")
    @Mapping(source = "allRecipesRecipe.prepTime", target = "head.time.prepTime")
    @Mapping(source = "allRecipesRecipe.totalTime", target = "head.time.totalTime")
    RecipeDto toRecipeDto(AllRecipesRecipe allRecipesRecipe);

}
