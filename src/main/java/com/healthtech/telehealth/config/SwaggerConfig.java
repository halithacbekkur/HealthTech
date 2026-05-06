package com.healthtech.telehealth.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI telehealthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Tele-Saglik API")
                        .description("Tele-Saglik Platformu Backend API Dokumantasyonu")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("HealthTech Team")))
                // JWT token ile test yapabilmek icin guvenlik semasi ekleniyor
                .addSecurityItem(new SecurityRequirement().addList("Bearer Token"))
                .components(new Components()
                        .addSecuritySchemes("Bearer Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                                        .description("JWT token girin (Bearer yazmaya gerek yok)")));
    }
}
