package com.spencer.recipeloader.scraper.service;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.scraper.model.AllRecipesRecipe;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScraperService {

    RecipeMapper recipeMapper;

    public ScraperService(RecipeMapper recipeMapper) {
        this.recipeMapper = recipeMapper;
    }

    public RecipeDto scrapeAllRecipes(String url) {

        Document doc;
        ObjectMapper mapper = new ObjectMapper();

        try {
            doc = Jsoup.connect(url).get();
            
            //Elements scriptElements = doc.getElementsByTag("script");
            Element jsonScript = doc.getElementsByAttributeValue("type", "application/ld+json").first();
            String data = cleanseRecipe(jsonScript.data());
            
            
            log.debug("HR element is {}", data);

            AllRecipesRecipe rec = mapper.readValue(data, AllRecipesRecipe.class);

            AllRecipesRecipe recipe = rec;

            RecipeDto result = recipeMapper.toRecipeDto(recipe);

            log.debug("done");

            return result;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String cleanseRecipe(String data) {
        if (data.startsWith("[")) {
            String working = StringUtils.stripStart(data, "[");
            String working2 = StringUtils.stripEnd(working, "]");
            return StringUtils.strip(working2); 
                //.substringBetween(jsonScript.data(), "[", "]");// jsonScript.data();
        } else {
            return data;
        }
    }

    public RecipeDto scrapeHungryRoot(String url) {

        Document doc;

        try {
            doc = Jsoup.connect(url).get();
            
            //Elements scriptElements = doc.getElementsByTag("script");
            Elements jsonScripts = doc.getElementsByAttributeValue("type", "application/ld+json");
            
            for (Element element :jsonScripts ){                
                log.debug("HR element is {}",element);   
                log.debug("done");
            }
            
            
            //Elements allEls = doc.getAllElements();
            //Elements results = allEls.select("div");
            //Elements elem = results.select("div,instructions");
            //Element recipe = doc.getElementById("recipe");
            //these don't work at all
            //Elements instructionsEls = doc.getElementsByClass("instructions-text");
            //Element masthead = doc.select("instructions-text").first();
            //doc.select("div").forEach(System.out::println);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }
}
