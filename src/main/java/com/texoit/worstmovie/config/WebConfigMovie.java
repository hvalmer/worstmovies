package com.texoit.worstmovie.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfigMovie implements WebMvcConfigurer {
	
	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry
				.addMapping("/api/**")
				.allowedMethods("POST", "GET", "PUT", "DELETE")
				.allowedMethods("*")
				.allowedOrigins("*")
				.maxAge(3600);
		registry
				.addMapping("/v2/api-docs")
				.allowedMethods("POST", "GET", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowedOrigins("*")
                .maxAge(3600);
	}
}
