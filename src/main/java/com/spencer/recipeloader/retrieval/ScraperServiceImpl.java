package com.spencer.recipeloader.retrieval;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.controller.ScrapeRequest;
import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.retrieval.image.ImageRetriever;
import com.spencer.recipeloader.retrieval.model.recipeml.ImageInfo;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;
import com.spencer.recipeloader.retrieval.model.scraper.AllRecipesRecipe;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ScraperServiceImpl implements RecipeRetrieverService<ScrapeRequest> {

    RecipeMapper recipeMapper;
    JSouper jsouper;
    ImageRetriever imageRetriever;

    public ScraperServiceImpl(RecipeMapper recipeMapper, JSouper jsouper, ImageRetriever imageRetriever) {
        this.recipeMapper = recipeMapper;
        this.jsouper = jsouper;
        this.imageRetriever = imageRetriever;
    }

    @Override
    public RecipeDto retrieveRecipe(ScrapeRequest input) {

        String url = input.getURL();
        
        ObjectMapper mapper = new ObjectMapper();
        Document doc;

        try {
            doc = jsouper.getDoc(url);
            
            Element jsonScript = doc.getElementsByAttributeValue("type", "application/ld+json").first();

            AllRecipesRecipe recipe = mapper.readValue(cleanseRecipe(jsonScript.data()), AllRecipesRecipe.class);

            ImageInfo imgInfo = downloadImage(doc);

            RecipeDto result = recipeMapper.toRecipeDto(recipe, imgInfo);

            return result;

        } catch (IOException e) {
            throw new RestClientException(e.getMessage());
        }
    } 

    private ImageInfo downloadImage(Document doc) {
        String imgSrc = doc.select("img").first().attr("data-src");

        ImageInfo imgInfo = imageRetriever.downloadImage(imgSrc);
        
        return imgInfo;
    }

    private String cleanseRecipe(String data) {
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
