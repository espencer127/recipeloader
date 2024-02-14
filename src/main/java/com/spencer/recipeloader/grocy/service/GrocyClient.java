package com.spencer.recipeloader.grocy.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
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

    private final RestClient grocyWebClient;
    private Integer grocyDockerPort;
    private RecipeMapper recipeMapper;
    private ObjectMapper mapper;

    public GrocyClient(RestClient grocyWebClient,
            @Value("${grocy.docker.port}") Integer grocyDockerPort,
            RecipeMapper recipeMapper, ObjectMapper mapper) {
        this.grocyWebClient = grocyWebClient;
        this.grocyDockerPort = grocyDockerPort;
        this.recipeMapper = recipeMapper;
        this.mapper = mapper;
    }

    public List<Product> getProducts() {
        ResponseEntity<String> result = getObject(Entity.PRODUCTS.label);
        
        if (result == null) {
            return null;
        }

        try {
            Product[] products = mapper.readValue(result.getBody(), Product[].class);

            List<Product> productList = Arrays.asList(products);

            List<Product> nonNullExistingProducts = productList
                    .stream()
                    .filter(x -> (!StringUtils.isEmpty(StringUtils.deleteWhitespace(x.getName()))))
                    .collect(Collectors.toList());

            return nonNullExistingProducts;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<Recipe> getRecipes() {
        ResponseEntity<String> result = getObject(Entity.RECIPES.label);

        try {
            Recipe[] recipes = mapper.readValue(result.getBody(), Recipe[].class);

            List<Recipe> recipeList = Arrays.asList(recipes);

            List<Recipe> nonNullExistingRecipes = recipeList
                    .stream()
                    .filter(x -> (!StringUtils.isEmpty(StringUtils.deleteWhitespace(x.getName()))))
                    .collect(Collectors.toList());

            return nonNullExistingRecipes;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<QuantityUnit> getQuantityUnits() {
        ResponseEntity<String> result = getObject(Entity.QUANTITY_UNITS.label);

        if (result == null) {
            return null;
        }

        try {
            QuantityUnit[] quantityUnits = mapper.readValue(result.getBody(), QuantityUnit[].class);

            List<QuantityUnit> productList = Arrays.asList(quantityUnits);

            List<QuantityUnit> nonNullExistingQUs = productList
                    .stream()
                    .filter(x -> (!StringUtils.isEmpty(StringUtils.deleteWhitespace(x.getName()))))
                    .collect(Collectors.toList());

            return nonNullExistingQUs;
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<RecipesPos> getRecipesPos() {
        ResponseEntity<String> result = getObject(Entity.RECIPES_POS.label);

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

            try {
                return grocyWebClient.get()
                        .uri(buildUri)
                        .retrieve()
                        .toEntity(String.class);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
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

        for (String qty : quantitiesWeNeedToAdd) {
            try {
                QuantityUnit postBody = recipeMapper.toQuantityUnitPostBody(qty);
                GrocyPostResponse apiResponse = postObject(Entity.QUANTITY_UNITS.label,
                        mapper.writeValueAsString(postBody));

                postBody.setId(apiResponse.getCreated_object_id());

                finalResponse.add(postBody);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        log.debug("created the qty units {}", finalResponse);

        return finalResponse;
    }

    /**
     * Add the specified "Product" entries to the Grocy database and return the
     * responses
     * 
     * @param ingredientsWeNeedToAdd
     * @return
     */
    public List<Product> createProducts(List<Product> ingredientsWeNeedToAdd) {
        List<Product> finalResponse = new ArrayList<>();

        for (Product ingredient : ingredientsWeNeedToAdd) {
            try {
                GrocyPostResponse apiResponse = postObject(Entity.PRODUCTS.label,
                        mapper.writeValueAsString(ingredient));

                ingredient.setId(apiResponse.getCreated_object_id());

                finalResponse.add(ingredient);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        log.debug("created the products {}", finalResponse);

        return finalResponse;
    }

    public Recipe createRecipe(Recipe recipe) {
        try {
            GrocyPostResponse apiResponse = postObject((Entity.RECIPES.label), mapper.writeValueAsString(recipe));
            log.debug("Got the response: {}", apiResponse);

            recipe.setId(apiResponse.getCreated_object_id());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        log.debug("returning the recipe {}", recipe);

        return recipe;
    }

    public void createRecipePos(List<RecipesPos> recipePosWeNeedToAdd) {
        Integer errorCt = 0;

        for (RecipesPos rp : recipePosWeNeedToAdd) {
            try {
                postObject((Entity.RECIPES_POS.label), mapper.writeValueAsString(rp));
            } catch (Exception e) {
                e.printStackTrace();
                errorCt++;
            }
        }

        log.debug("we did it fam :)");

        if (errorCt > 0) {
            log.warn("We goofed at least once :/ we goofed {} times actually", errorCt);
        }
    }

    @SuppressWarnings("null")
    public GrocyPostResponse postObject(String entity, String body) {

        String api = "api";
        String objects = "objects";
        GrocyPostResponse grocyResponse = new GrocyPostResponse();
        List<String> errors = new ArrayList<>();;

        URI buildUri = UriComponentsBuilder.fromUriString("http://localhost")
                .port(grocyDockerPort)
                .pathSegment(api, objects, entity)
                .build().encode().toUri();

        log.debug("Sending request to {} with the body {}", buildUri, body);

        //grocyWebClient.mutate().filter(GrocyClientExceptionHandler.errorHandlingFilter());

        try {
            grocyResponse = grocyWebClient.post()
                    .uri(buildUri)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(body)
                    .retrieve()
                    //need to make an 'errorhandler' instead of response
                            //response -> new Exception(response.toString()))// response.bodyToMono(String.class).map(Exception::new))
                    // (response -> response.bodyToMono(String.class).flatMap(error ->
                    // Mono.error(new Exception(error))
                    // // //response -> doSomething(response))

                    // .onStatus(httpStatus -> httpStatus.value() != 200,
                    // error -> Mono.error(new Exception("Erorr fulfilling POST request")))
                    .toEntity(GrocyPostResponse.class).getBody();
        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(),
                    "Provided qu_id doesn't have a related conversion for that product")) {
                log.error(e.getMessage());
                errors.add(
                        "We were unable to associate an ingredient to this recipe. To fix this issue, first create a conversion between the Grocy product's quantity and the recipe ingredient's quantity. Then re-run a POST call to create the recipe_pos with the URI" + buildUri + " and body " +
                        body);
            } else {
                errors.add(e.getMessage());
            }

        }

        if (!errors.isEmpty()) {    
            String[] joinedMsg = {StringUtils.join(errors, "; ")};
            String concatArr = StringUtils.join(joinedMsg, "; ");
            throw new RestClientException(concatArr);
        }

        return grocyResponse;
    }

    @SuppressWarnings("null")
    public GrocyPostResponse putFile(String entity, String fileName, byte[] bytes) {

        String api = "api";
        String files = "files";
        GrocyPostResponse grocyResponse = new GrocyPostResponse();
        List<String> errors = new ArrayList<>();;

        URI buildUri = UriComponentsBuilder.fromUriString("http://localhost")
                .port(grocyDockerPort)
                .pathSegment(api, files, entity, fileName)
                .build().encode().toUri();

        log.debug("Sending request to {} with the body {}", buildUri, bytes);

        //grocyWebClient.mutate().filter(GrocyClientExceptionHandler.errorHandlingFilter());

        try {
            grocyResponse = grocyWebClient.put()
                    .uri(buildUri)
                    .header(HttpHeaders.CONTENT_TYPE, "application/octet-stream")
                    .body(bytes)
                    .retrieve()
                    //need to make an 'errorhandler' instead of response
                            //response -> new Exception(response.toString()))// response.bodyToMono(String.class).map(Exception::new))
                    // (response -> response.bodyToMono(String.class).flatMap(error ->
                    // Mono.error(new Exception(error))
                    // // //response -> doSomething(response))

                    // .onStatus(httpStatus -> httpStatus.value() != 200,
                    // error -> Mono.error(new Exception("Erorr fulfilling POST request")))
                    .toEntity(GrocyPostResponse.class).getBody();
        } catch (Exception e) {
            if (StringUtils.contains(e.getMessage(),
                    "Provided qu_id doesn't have a related conversion for that product")) {
                log.error(e.getMessage());
                errors.add(
                        "We were unable to associate an ingredient to this recipe. To fix this issue, first create a conversion between the Grocy product's quantity and the recipe ingredient's quantity. Then re-run a POST call to create the recipe_pos with the URI" + buildUri + " and body " +
                        bytes);
            } else {
                errors.add(e.getMessage());
            }

        }

        if (!errors.isEmpty()) {    
            String[] joinedMsg = {StringUtils.join(errors, "; ")};
            String concatArr = StringUtils.join(joinedMsg, "; ");
            throw new RestClientException(concatArr);
        }

        return grocyResponse;
    }


}
