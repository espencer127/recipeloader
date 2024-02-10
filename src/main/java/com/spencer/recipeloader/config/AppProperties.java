package com.spencer.recipeloader.config;

import org.springframework.context.annotation.PropertySource;

@PropertySource("classpath:foo.properties")
@PropertySource("classpath:bar.properties")
public class AppProperties {

}
