package com.spencer.recipeloader.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.universal.model.RecipeInfo;

public class RecipeMapperImplTests {

    @Disabled("I've disabled this functionality in the app right now")
    @Test
    public void whenParseDtoToRecipe_shouldContainCategories() {
        
        ObjectMapper objectMapper = new ObjectMapper();
        RecipeMapper recipeMapper = Mappers.getMapper(RecipeMapper.class);
        RecipeInfo recipeDto = new RecipeInfo();

        try {
            recipeDto = objectMapper.readValue(new File("src/test/resources/mockobjects/grocy/service/recipeDto.json"), RecipeInfo.class);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        Recipe mapperResult = recipeMapper.toRecipe(recipeDto, "dummy");
        
        assertEquals("Cookies,And,Bars", mapperResult.getUserfields().getCategory());
    }
}
