package com.spencer.recipeloader.grocy.service;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spencer.recipeloader.grocy.model.Product;
import com.spencer.recipeloader.grocy.model.QuantityUnit;
import com.spencer.recipeloader.grocy.model.Recipe;
import com.spencer.recipeloader.recipeml.model.Ing;

@Component
public class GrocyClient {

    private final WebClient grocyWebClient;
    private Integer grocyDockerPort;

    @Autowired
    public GrocyClient(WebClient grocyWebClient,
            @Value("${grocy.docker.port}") Integer grocyDockerPort) {
        this.grocyWebClient = grocyWebClient;
        this.grocyDockerPort = grocyDockerPort;
    }

    public List<Product> getProducts() {
        ResponseEntity<String> result = getObject(Entity.PRODUCTS.label);
        ObjectMapper mapper = new ObjectMapper();

        try {
            Product[] products = mapper.readValue(result.getBody(), Product[].class);
        
            List<Product> productList = Arrays.asList(products);
            return productList;
        } catch (JsonMappingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
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
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

    public ResponseEntity<String> getObject(String entity) {

        String objects = "objects";

        URI buildUri = UriComponentsBuilder.fromUriString("http://localhost")
        .port(grocyDockerPort)
        .pathSegment(objects, entity)
        .path("/")
        .build().encode().toUri();

        ResponseEntity<String> grocyResponse = grocyWebClient.get()
        .uri(buildUri)
        .retrieve()
        .toEntity(String.class)
        .block();
        
        return grocyResponse;
    }
    
    public Ing[] getIngredients() {
        return null;
    }


}
