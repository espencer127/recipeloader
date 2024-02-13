package com.spencer.recipeloader.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.service.GrocyService;
import com.spencer.recipeloader.retrieval.ScraperServiceImpl;
import com.spencer.recipeloader.retrieval.model.recipeml.RecipeDto;


@Controller
public class UIController {

    ApiController apiController;
    ScraperServiceImpl scraperService;
    GrocyService grocyService;

    public UIController(ApiController apiController, ScraperServiceImpl scraperService, GrocyService grocyService) {
        this.apiController = apiController;
        this.scraperService = scraperService;
        this.grocyService = grocyService;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @RequestMapping("/index")
    public String indexUrl(Model model) {
        return "index";
    }

    @RequestMapping(value = "/ui/scrapeExample", method = RequestMethod.GET)
    public String getInfo(Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String exampleUrl = "https://www.allrecipes.com/recipe/235158/worlds-best-honey-garlic-pork-chops/";
        ScrapeRequest req = new ScrapeRequest();
        req.setURL(exampleUrl);
        RecipeDto result = scraperService.retrieveRecipe(req);
        model.addAttribute("recipeDto", mapper.writeValueAsString(result));
        model.addAttribute("url", exampleUrl);
        return "ScrapeExample";
    }

    @RequestMapping(value="/ui/scrape", method = RequestMethod.GET)
    public String showForm(Model model) {
        model.addAttribute("scrapeRequest", new ScrapeRequest());

        return "ScrapeForm";
    }

    @RequestMapping(value="/ui/scrapeRequest", method = RequestMethod.POST)
    public String showRequest(@ModelAttribute("scrapeRequest") ScrapeRequest request, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String url = request.getURL();
        RecipeDto result = scraperService.retrieveRecipe(request);
        model.addAttribute("recipeDto", mapper.writeValueAsString(result));
        model.addAttribute("url", url);

        return "ScrapedRecipe";
    }

    @RequestMapping(value="/ui/insertScrape", method = RequestMethod.POST)
    public String insertRecipe(@ModelAttribute("recipeDto") RecipeDto scrape, Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        grocyService.sendInfoToGrocy(scrape);
        
        model.addAttribute("recipeDto", mapper.writeValueAsString(scrape));

        return "InsertResult";
    }


}
