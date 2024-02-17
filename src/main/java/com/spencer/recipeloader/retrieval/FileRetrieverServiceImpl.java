package com.spencer.recipeloader.retrieval;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spencer.recipeloader.controller.ImportRequest;
import com.spencer.recipeloader.retrieval.model.recipeml.Ing;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeMLWrapper;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileRetrieverServiceImpl implements RecipeRetrieverService<ImportRequest> {

    public FileRetrieverServiceImpl() {
    }

    /**
     * I had to do some weird stuff here because RecipeML spec allows multiple 
     * entries of the same property and Jackson XLMMapper doesn't like that. 
     * So i had to Deserialize XML from the file into a string, then a Java object
     * @return
     */
    @Override
    public RecipeDto retrieveRecipe(ImportRequest input) {

        File file = new File(input.getFilePath());
        
        ObjectMapper mapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            String xml = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jObject = XML.toJSONObject(xml);
            Object json = mapper.readValue(jObject.toString(), Object.class);
            String output = mapper.writeValueAsString(json);
            RecipeMLWrapper recipe = mapper.readValue(output, RecipeMLWrapper.class);
            
            log.debug("Got recipe {}", recipe);

            fixRecipe(recipe);

            return recipe.getRecipeml().getRecipe();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * If the recipe has any ingredients with empty units, fill it with 'unit'
     * @param recipe
     */
    private void fixRecipe(RecipeMLWrapper recipe) {
        for (Ing ing : recipe.getRecipeml().getRecipe().getIngredients().getIng()) {
            if (StringUtils.isBlank(ing.getAmt().getUnit())) {
                ing.getAmt().setUnit("Unit");
            }
        }
    }

}
