package com.hotel.form.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI formServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Hotel Management System - Guest Feedback Service API")
                        .description("Comprehensive API documentation for Guest Feedback Service. This service manages guest feedback, reviews, and satisfaction surveys.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Hotel Management Team")
                                .email("support@hotelmanagement.com")
                                .url("https://hotelmanagement.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8085")
                                .description("Development Server"),
                        new Server()
                                .url("https://api.hotelmanagement.com/feedbacks")
                                .description("Production Server")
                ))
                .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("Bearer Authentication",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT Token for authentication")));
    }
}
