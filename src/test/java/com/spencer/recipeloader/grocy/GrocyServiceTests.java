package com.spencer.recipeloader.grocy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.grocy.service.GrocyClient;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.mapper.RecipeMapperImpl;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.recipeml.service.RecipeMLService;

public class GrocyServiceTests {

    private RecipeMLService recipeMLService;

    public RecipeMapper recipeMapper;

    @Mock
    public GrocyClient grocyClient;

    /**
     * Right now the 'generateRecipePosList' makes double associations per product. Let's troubleshoot that.
     */
    @Test
    public void whenGenerateRecipePosList_ShouldOnlyCreateOneRecipePosPerProduct() {

        recipeMLService = new RecipeMLService("src\\test\\resources\\BrowniesRecipe.xml");

        RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);

        GrocyService grocyService = new GrocyService(recipeMLService, recipeMapper, grocyClient);
        
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            RecipeDto recipeDto = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/recipeDto.json"), RecipeDto.class);
            Recipe recipe = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/recipe.json"), Recipe.class);
            Product[] updatedProductsArray = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/updatedProductsList.json"), Product[].class);
            QuantityUnit[] updatedQuantityArray = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/updatedQuantityUnits.json"), QuantityUnit[].class);

            List<Product> updatedProductsList = Arrays.asList(updatedProductsArray);
            List<QuantityUnit> updatedQuantityUnits = Arrays.asList(updatedQuantityArray);

            List<RecipesPos> result = grocyService.generateRecipePosList(recipeDto, recipe,
                updatedProductsList, updatedQuantityUnits);

                //should be 9
            assertEquals(updatedProductsList.size(), result.size());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
