package com.spencer.recipeloader.controller;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.Rendering;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.recipeml.model.RecipeDto;
import com.spencer.recipeloader.scraper.service.ScraperService;

import reactor.core.publisher.Mono;

@Controller
public class UIController {

    ApiController apiController;
    ScraperService scraperService;

    public UIController(ApiController apiController, ScraperService scraperService) {
        this.apiController = apiController;
        this.scraperService = scraperService;
    }

    @GetMapping("/")
    public Mono<Rendering> home() {
        return Mono.just(Rendering.view("index").build());
    }

    @RequestMapping("/index")
    public String indexUrl(Model model) {
        return "index";
    }

    @RequestMapping(value = "/scrapeExample", method = RequestMethod.GET)
    public String getInfo(Model model) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String exampleUrl = "https://www.allrecipes.com/recipe/235158/worlds-best-honey-garlic-pork-chops/";
        RecipeDto result = scraperService.scrapeAllRecipes(exampleUrl);
        model.addAttribute("recipeDto", mapper.writeValueAsString(result));
        model.addAttribute("url", exampleUrl);
        return "ScrapeExample";
    }


}
