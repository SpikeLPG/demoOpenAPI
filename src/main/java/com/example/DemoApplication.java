package com.example;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication()
@Configuration
public class DemoApplication {

    public static void main(String[] args) {
        new SpringApplication(DemoApplication.class).run(args);
    }

    //Used by the
    @Bean
    public OpenAPI customOpenAPI(@Value("1.0.0") String appVersion) {
        return new OpenAPI().info(new Info()
                .title("Example API Documentation")
                .version(appVersion)
                .description("""
                        This is a demo application to try different ways of producing API documentation:
                         * [SpringDoc OpenAPI](https://springdoc.org/v2/#getting-started) using OpenAPI 3 spring boot library
                         * [Spring RestDocs](https://spring.io/projects/spring-restdocs) using Spring Rest Docs library
                         """
                )
                .termsOfService("https://swagger.io/terms/")
                .license(new License().name("Apache 2.0")
                        .url("https://springdoc.org")));
    }
}
