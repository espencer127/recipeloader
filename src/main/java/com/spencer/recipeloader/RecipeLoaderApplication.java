package com.spencer.recipeloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spencer.recipeloader.grocy.service.GrocyService;

@SpringBootApplication
public class RecipeLoaderApplication {

	@Autowired
	GrocyService grocyService;
	
	public static void main(String[] args) {
		SpringApplication.run(RecipeLoaderApplication.class, args);
	}

}
