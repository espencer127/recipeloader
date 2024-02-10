package com.spencer.recipeloader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Value("${grocy.api-key}")
    private String API_KEY;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .defaultHeader("GROCY-API-KEY", API_KEY)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

}
