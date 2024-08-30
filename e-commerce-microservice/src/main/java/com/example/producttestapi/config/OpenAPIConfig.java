package com.example.producttestapi.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Hamza Azeem",
                        email = "hamzaalsherif9@gmail.com"
                ),
                title = "Open API - E-Commerce Documentation"
        )
)
public class OpenAPIConfig {
}
