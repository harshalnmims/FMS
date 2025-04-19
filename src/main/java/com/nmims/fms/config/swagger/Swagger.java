package com.nmims.fms.config.swagger;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class Swagger {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FMS API")
                        .version("1.0")
                        .description("This is the backend API for Flying Management System (FMS)"));
    }

    // Optional: Grouping APIs
    // @Bean
    // public GroupedOpenApi publicApi() {
    //     return GroupedOpenApi.builder()
    //             .group("public-apis")
    //             .pathsToMatch("/api/**")
    //             .build();
    // }
}
