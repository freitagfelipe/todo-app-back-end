package com.freitagfelipe.todoapp;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openApi() {
        Info info = new Info()
            .description("API documentation.")
            .title("Todo App");

        return new OpenAPI()
            .info(info);
    }

}
