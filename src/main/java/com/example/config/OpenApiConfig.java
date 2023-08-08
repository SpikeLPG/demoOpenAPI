package com.example.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final Info API_INFO = new Info()
            .title("Example API Documentation")
            .version("1.2.3")
            .description("""
                    This is a demo application to try different ways of producing API documentation:
                     * [SpringDoc OpenAPI](https://springdoc.org/v2/#getting-started) using OpenAPI 3 spring boot library
                     * [Spring RestDocs](https://spring.io/projects/spring-restdocs) using Spring Rest Docs library
                     """
            )
            .termsOfService("https://swagger.io/terms/")
            .license(new License()
                    .name("Apache 2.0")
                    .url("https://springdoc.org"));

    @Bean
    public OpenAPI bookOpenAPI() {
        return new OpenAPI().info(API_INFO);
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public-docs")
                .displayName("Publicly Accessible Specifications")
                .pathsToExclude("/**/internal/**", "/**/songs/**")
                .build();
    }

    @Bean
    public GroupedOpenApi privateApi() {
        return GroupedOpenApi.builder()
                .group("internal-docs")
                .displayName("Internally Accessible Specifications")
                .pathsToExclude("/**/songs/**")
                .build();
    }
}
