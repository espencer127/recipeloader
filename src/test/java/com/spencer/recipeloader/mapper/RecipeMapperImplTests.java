package com.spencer.recipeloader.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;

public class RecipeMapperImplTests {

    @Test
    public void whenParseDtoToRecipe_shouldContainCategories() {
        
        ObjectMapper objectMapper = new ObjectMapper();
        RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);
        RecipeDto recipeDto = new RecipeDto();

        try {
            recipeDto = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/recipeDto.json"), RecipeDto.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Recipe mapperResult = recipeMapper.toRecipe(recipeDto);
        
        assertEquals("Cookies,And,Bars", mapperResult.getUserfields().getCategory());
    }
}
