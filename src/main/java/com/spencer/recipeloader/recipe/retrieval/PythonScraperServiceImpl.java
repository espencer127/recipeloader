package com.spencer.recipeloader.recipe.retrieval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.controller.ScrapeRequest;
import com.spencer.recipeloader.recipe.retrieval.model.pythonscraper.PythonRecipe;
import com.spencer.recipeloader.universal.model.FullResponse;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PythonScraperServiceImpl implements RecipeRetrieverService<ScrapeRequest> {

    @Override
    public FullResponse retrieveRecipe(ScrapeRequest requestBody) {
        String result = "";
        String errors = "";
        Runtime rt = Runtime.getRuntime();
        PythonRecipe recipe = new PythonRecipe();
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);

        StringBuilder command = new StringBuilder("python C:/users/evani/code/recipeloader/src/main/resources/python/recipe-scrapers");

        if (requestBody.getWildWestMode() != null && requestBody.getWildWestMode() == true) {
            command.append("/wildScraper.py " + requestBody.getURL());
        } else {
            command.append("/scraper.py " + requestBody.getURL());
        }

        String line = command.toString();

        Process proc;

        try {
            proc = rt.exec(line);
            
            BufferedReader resultBR = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            result = resultBR.lines().collect(Collectors.joining());

            resultBR.close();

            BufferedReader errorBR = new BufferedReader(new InputStreamReader(proc.getErrorStream()));

            errors = errorBR.lines().collect(Collectors.joining());

            errorBR.close();

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        log.debug("Got the recipe: {}", result);
        log.debug("got errors: {}", errors);

        //TODO: convert the 'result' to the 'python scraper schema' 
        try {
            recipe = mapper.readValue(cleanseBeforeJsonMapping(result), PythonRecipe.class);
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return new FullResponse().buildFromPythonRecipe(recipe);
    }

    private String cleanseBeforeJsonMapping(String input) {
        return StringUtils.replace(input, "None", "null");
    }

}
