package com.ecommerce.cart.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-commerce Cart API")
                        .version("1.0")
                        .description("API para la gestión de carritos de compras"));
    }
}
