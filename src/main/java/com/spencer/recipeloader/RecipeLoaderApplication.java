package com.spencer.recipeloader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.spencer.recipeloader.grocy.service.GrocyService;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class RecipeLoaderApplication implements CommandLineRunner {

	@Autowired
	GrocyService grocyService;
	
	public static void main(String[] args) {
		SpringApplication.run(RecipeLoaderApplication.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		System.out.println("hi :)");

		//grocyService.execute();
	}

}
