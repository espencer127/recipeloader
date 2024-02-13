package com.spencer.recipeloader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class WebClientConfig {

    @Value("${grocy.api-key}")
    private String API_KEY;

    @Bean
    public RestClient webClient() {
        return RestClient.builder()
                .defaultHeader("GROCY-API-KEY", API_KEY)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

}
