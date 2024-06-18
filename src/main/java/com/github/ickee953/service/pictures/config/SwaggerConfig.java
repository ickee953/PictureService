package com.github.ickee953.service.pictures.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

    @Bean
    public OpenAPI swaggerApiConfig() {
        Info info = new Info();
        info.title("Pictures API");
        info.description("Spring Boot Project to demonstrate Swagger UI integration ");
        info.version("1.0");

        return new OpenAPI().info(info);
    }

}
