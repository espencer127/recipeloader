package com.spencer.recipeloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.spencer.recipeloader.mapper.RecipeMapper;
import com.spencer.recipeloader.mapper.RecipeMapperImpl;

@Configuration
public class RecipeMapperConfig {

    @Bean
    public RecipeMapper recipeMapper() {
        return new RecipeMapperImpl();
    }
}
