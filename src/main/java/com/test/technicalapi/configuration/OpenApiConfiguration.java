package com.test.technicalapi.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfiguration {
    @Bean(name = "technicalOpenApi")
    public OpenAPI technicalOpenApi() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("API")
                        .description("OpenAPI 3.0"));
    }
}
