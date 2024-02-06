package com.spencer.recipeloader.recipeml.service;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.spencer.recipeloader.recipeml.model.RecipeMLWrapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RecipeMLService {

    String recipePath;

    public RecipeMLService(@Value("${recipe.file.path}") String recipePath) {
        this.recipePath = recipePath;
    }

    /**
     * I had to do some weird stuff here because RecipeML spec allows multiple entries of the same property and Jackson XLMMapper doesn't like that
     * @return
     */
    public RecipeMLWrapper retrieveRecipe() {
        File file = new File(recipePath);

        // Create mappers
        ObjectMapper mapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Deserialize XML from the file into a Java object
        try {
            String xml = FileUtils.readFileToString(file, "UTF-8");
            JSONObject jObject = XML.toJSONObject(xml);
            Object json = mapper.readValue(jObject.toString(), Object.class);
            String output = mapper.writeValueAsString(json);
            RecipeMLWrapper recipe = mapper.readValue(output, RecipeMLWrapper.class);
            
            log.info("Got recipe {}", recipe);

            return recipe;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;

    }

}
