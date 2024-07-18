package com.example.producttestapi;

import com.github.javafaker.Faker;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class AbstractTestcontainers {
    @Container
    public static MySQLContainer<?> mySQLContainer = new MySQLContainer<>
            ("mysql:latest")
            .withDatabaseName("producttestapi")
            .withPassword("root")
            .withUsername("root");
    protected Faker faker = new Faker();

    @DynamicPropertySource
    private static void setProperties(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.datasource.url",
                mySQLContainer::getJdbcUrl
        );
        registry.add(
                "spring.datasource.username",
                mySQLContainer::getUsername
        );
        registry.add(
                "spring.datasource.password",
                mySQLContainer::getPassword
        );
    }
}
