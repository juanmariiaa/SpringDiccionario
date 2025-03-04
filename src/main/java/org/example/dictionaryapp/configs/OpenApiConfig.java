package org.example.dictionaryapp.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Dictionary App",
                version = "1.0",
                description = "Dictionary App API"
        )
)
public class OpenApiConfig {
}
