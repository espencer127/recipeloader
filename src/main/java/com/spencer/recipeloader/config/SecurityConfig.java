package com.spencer.recipeloader.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean(name = {"securityFilterChain"})
    public SecurityFilterChain springSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(exchanges -> exchanges
                .requestMatchers("/*").permitAll()
                .anyRequest().authenticated()
            )
            .csrf(customizer -> customizer.disable());

        return http.build();
    }

}