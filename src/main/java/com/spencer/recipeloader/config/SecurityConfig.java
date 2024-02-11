package com.spencer.recipeloader.config;


import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

	private static final String[] excludedAuthPages = {
		"/test/excludedAuthPages",
		"/swagger-ui/**",
		"/v2/api-docs/**",
		"/swagger-resources/**"
	};
	
	//TODO: This doesn't work how i want, all pages still requre a login
    @Bean
	public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
		http
			.authorizeExchange(exchanges -> exchanges
				.pathMatchers(excludedAuthPages)
			    //.anyExchange
				.permitAll()
			);
		return http.build();
	}
}