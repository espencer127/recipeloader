package com.spencer.recipeloader.grocy.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.spencer.recipeloader.recipeml.model.Ing;

@Service
public class GrocyService {

    private String grocyDockerPort;

    @Autowired
    public GrocyService(@Value("${grocy.docker.port}") String grocyDockerPort) {
        this.grocyDockerPort = grocyDockerPort;
    }

    public Ing[] getIngredients() {
        RestClient restClient = RestClient.create();
        return null;
    }

}
