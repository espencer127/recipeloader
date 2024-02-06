package com.spencer.recipeloader.grocy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import com.spencer.recipeloader.grocy.service.GrocyClient;

import reactor.core.publisher.Mono;

public class GrocyServiceTests {

    private GrocyClient grocyClient;

    /*
     * @BeforeAll
     * public void setup() {
     * grocyClient = new GrocyClient(new WebClient(), 2424);
     * 
     * }
     */

    @SuppressWarnings("null")
    @Test
    public void whenCallGetObject_ShouldReturnString() {

        try {
            String filePath = "src\\test\\resources\\grocyresponses\\GETObjectsProductsAPIResponse.json";
            String entity = "products";

            String PRODUCTS_RESPONSE_BODY_STRING = FileUtils.readFileToString(new File(filePath), "UTF-8");

            ClientResponse respWithString = ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(PRODUCTS_RESPONSE_BODY_STRING).build();

            WebClient webClient = WebClient.builder()
                    .exchangeFunction(x -> Mono.just(respWithString))
                    .build();

            this.grocyClient = new GrocyClient(webClient, 2128);

            assertEquals(ResponseEntity.class, grocyClient.getObject(entity).getClass());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @SuppressWarnings("null")
    @Test
    public void whenCallGetProduct_ShouldReturnProducts() {
        try {
            String filePath = "src\\test\\resources\\grocyresponses\\GETObjectsProductsAPIResponse.json";

            String PRODUCTS_RESPONSE_BODY_STRING = FileUtils.readFileToString(new File(filePath), "UTF-8");

            ClientResponse respWithString = ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(PRODUCTS_RESPONSE_BODY_STRING).build();

            WebClient webClient = WebClient.builder()
                    .exchangeFunction(x -> Mono.just(respWithString))
                    .build();

            this.grocyClient = new GrocyClient(webClient, 2128);

            assertNotNull(grocyClient.getProducts());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("null")
    @Test
    public void whenCallGetRecipes_ShouldReturnRecipes() {
        try {
            String filePath = "src\\test\\resources\\grocyresponses\\GETObjectsRecipesAPIResponse.json";

            String RECIPES_RESPONSE_BODY_STRING = FileUtils.readFileToString(new File(filePath), "UTF-8");

            ClientResponse respWithString = ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(RECIPES_RESPONSE_BODY_STRING).build();

            WebClient webClient = WebClient.builder()
                    .exchangeFunction(x -> Mono.just(respWithString))
                    .build();

            this.grocyClient = new GrocyClient(webClient, 2128);

            assertNotNull(grocyClient.getRecipes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("null")
    @Test
    public void whenCallGetQuantityUnits_ShouldReturnQuantityUnits() {
        try {
            String filePath = "src\\test\\resources\\grocyresponses\\GETObjectsQuantityUnitsAPIResponse.json";

            String QUANTITY_UNITS_RESPONSE_BODY_STRING = FileUtils.readFileToString(new File(filePath), "UTF-8");

            ClientResponse respWithString = ClientResponse.create(HttpStatus.OK)
                    .header("Content-Type", MediaType.APPLICATION_JSON_VALUE)
                    .body(QUANTITY_UNITS_RESPONSE_BODY_STRING).build();

            WebClient webClient = WebClient.builder()
                    .exchangeFunction(x -> Mono.just(respWithString))
                    .build();

            this.grocyClient = new GrocyClient(webClient, 2128);

            assertNotNull(grocyClient.getRecipes());

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
