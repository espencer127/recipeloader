package com.spencer.recipeloader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${grocy.api-key}")
    private String API_KEY;

    @Primary
    @Bean(name="webClient")
    public RestClient webClient() {
        return RestClient.builder()
                .defaultHeader("GROCY-API-KEY", API_KEY)
                .defaultHeader(HttpHeaders.ACCEPT, "application/json")
                .build();
    }

    @Bean(name="putClient")
    public RestClient putClient() {
        return RestClient.builder()
                .defaultHeader("GROCY-API-KEY", API_KEY)
                .defaultHeader(HttpHeaders.ACCEPT, "application/octet-stream")
                .build();
    }

}
