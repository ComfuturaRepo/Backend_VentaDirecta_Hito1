package com.backend.comfutura.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")                          // Todas las rutas de la API
                        .allowedOrigins(
                                "http://localhost:4200",               // Angular en desarrollo
                                "http://localhost:4201"            // si usas otro puerto
                        )
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS", "HEAD")
                        .allowedHeaders("*")                        // Todos los headers
                        .allowCredentials(true)                     // Importante si usas cookies o auth con credenciales
                        .maxAge(3600);                              // Cache de preflight 1 hora
            }
        };
    }
}