package com.spencer.recipeloader.grocy.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Entity;
import com.spencer.recipeloader.grocy.model.GrocyPostResponse;
import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.grocy.model.RecipesPos;
import com.spencer.recipeloader.mapper.RecipeMapper;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class GrocyClient {

    private final WebClient grocyWebClient;
    private Integer grocyDockerPort;
    private RecipeMapper recipeMapper;

    public GrocyClient(WebClient grocyWebClient,
            @Value("${grocy.docker.port}") Integer grocyDockerPort,
            RecipeMapper recipeMapper) {
        this.grocyWebClient = grocyWebClient;
        this.grocyDockerPort = grocyDockerPort;
        this.recipeMapper = recipeMapper;
    }

    public List<Product> getProducts() {
        ResponseEntity<String> result = getObject(Entity.PRODUCTS.label);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Product[] products = mapper.readValue(result.getBody(), Product[].class);

            List<Product> productList = Arrays.asList(products);
            return productList;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Recipe> getRecipes() {
        ResponseEntity<String> result = getObject(Entity.RECIPES.label);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Recipe[] recipes = mapper.readValue(result.getBody(), Recipe[].class);

            List<Recipe> productList = Arrays.asList(recipes);
            return productList;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<QuantityUnit> getQuantityUnits() {
        ResponseEntity<String> result = getObject(Entity.QUANTITY_UNITS.label);
        ObjectMapper mapper = new ObjectMapper();

        try {
            QuantityUnit[] quantityUnits = mapper.readValue(result.getBody(), QuantityUnit[].class);

            List<QuantityUnit> productList = Arrays.asList(quantityUnits);
            return productList;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<RecipesPos> getRecipesPos() {
        ResponseEntity<String> result = getObject(Entity.RECIPES_POS.label);
        ObjectMapper mapper = new ObjectMapper();

        try {
            RecipesPos[] recipesPoss = mapper.readValue(result.getBody(), RecipesPos[].class);

            List<RecipesPos> recipesPossList = Arrays.asList(recipesPoss);
            return recipesPossList;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<String> getObject(String entity) {

        String api = "api";
        String objects = "objects";

        URI buildUri = UriComponentsBuilder.fromUriString("http://localhost")
                .port(grocyDockerPort)
                .pathSegment(api, objects, entity)
                .build().encode().toUri();

        ResponseEntity<String> grocyResponse = grocyWebClient.get()
                .uri(buildUri)
                .retrieve()
                .toEntity(String.class)
                .block();
        
        log.debug("got the object {}", grocyResponse);

        return grocyResponse;
    }
    
    /**
     * Add the specified "Quantity Unit" entries to the Grocy database and return
     * the responses
     * 
     * @param quantitiesWeNeedToAdd
     * @return
     */
    public List<QuantityUnit> createQuantityUnits(List<String> quantitiesWeNeedToAdd) {
        List<QuantityUnit> finalResponse = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();

            for (String qty : quantitiesWeNeedToAdd) {
                QuantityUnit postBody = recipeMapper.toQuantityUnitPostBody(qty);
                GrocyPostResponse apiResponse = postObject(Entity.QUANTITY_UNITS.label, mapper.writeValueAsString(postBody));

                postBody.setId(apiResponse.getCreated_object_id());

                finalResponse.add(postBody);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.debug("created the qty units {}", finalResponse);

        return finalResponse;
    }

    /**
     * Add the specified "Product" entries to the Grocy database and return the responses
     * 
     * @param ingredientsWeNeedToAdd
     * @return
     */
    public List<Product> createProducts(List<String> ingredientsWeNeedToAdd) {
        List<Product> finalResponse = new ArrayList<>();

        try {
            ObjectMapper mapper = new ObjectMapper();

            for (String ingredient : ingredientsWeNeedToAdd) {
                Product postBody = recipeMapper.toProductPostBody(ingredient);
                GrocyPostResponse apiResponse = postObject(Entity.PRODUCTS.label, mapper.writeValueAsString(postBody));

                postBody.setId(apiResponse.getCreated_object_id());

                finalResponse.add(postBody);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.debug("created the products {}", finalResponse);

        return finalResponse;
    }

    @SuppressWarnings("null")
    public GrocyPostResponse postObject(String entity, String body) {

        String api = "api";
        String objects = "objects";

        URI buildUri = UriComponentsBuilder.fromUriString("http://localhost")
                .port(grocyDockerPort)
                .pathSegment(api, objects, entity)
                .build().encode().toUri();

        log.debug("Sending request to {} with the body {}", buildUri, body);

        GrocyPostResponse grocyResponse = grocyWebClient.post()
                .uri(buildUri)
                .header(HttpHeaders.CONTENT_TYPE, "application/json")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(GrocyPostResponse.class)
                .block();

        return grocyResponse;
    }


}
