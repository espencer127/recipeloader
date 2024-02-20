package com.spencer.recipeloader.grocy;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.grocy.service.GrocyClient;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.image.retrieval.ImageRetriever;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipe.retrieval.FileRetrieverServiceImpl;
import com.spencer.recipeloader.universal.model.RecipeInfo;
import com.spencer.recipeloader.utils.Utils;

public class GrocyServiceTests {

    private FileRetrieverServiceImpl recipeMLService;

    public RecipeMapper recipeMapper;

    @Mock
    public GrocyClient grocyClient;

    public ImageRetriever imgRetriever;

    /**
     * Right now the 'generateRecipePosList' makes double associations per product. Let's troubleshoot that.
     */
    @Test
    public void whenGenerateRecipePosList_ShouldOnlyCreateOneRecipePosPerProduct() {

        recipeMLService = new FileRetrieverServiceImpl();

        RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);

        GrocyService grocyService = new GrocyService(recipeMLService, recipeMapper, grocyClient, imgRetriever);
        
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            RecipeInfo recipeDto = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/recipeDto.json"), RecipeInfo.class);
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

    @Disabled("how do we want to handle this post-refactor?")
    @Test
    public void whenParseQuantityString_shouldWork() throws Exception {
        String fractionQty = "1 1/2";
        Integer expected = 2;

        recipeMLService = new FileRetrieverServiceImpl();

        RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);

        GrocyService grocyService = new GrocyService(recipeMLService, recipeMapper, grocyClient, imgRetriever);
        
        Integer response = Utils.parseQty(fractionQty);

        assertEquals(expected, response);
    }

}
