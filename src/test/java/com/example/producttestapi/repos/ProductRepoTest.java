package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ProductRepoTest {
    @Autowired
    private ProductRepo underTest;
    @Autowired
    private CategoryRepo categoryRepo;
    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
        categoryRepo.deleteAll();
    }

    @Test
    void findByCategoryTestWillReturnListOfProducts() {
        // Arrange
        Category category = new Category("elect");
        Category savedCategory = categoryRepo.save(category);
        String name = faker.book().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 3, 5);
        Product product = new Product(
                name,
                description,
                price,
                category
        );
        // Act
        underTest.save(product);
        List<Product> actual = underTest.findByCategory(savedCategory.getId());
        // Assert
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getName()).isEqualTo(name);
        assertThat(actual.get(0).getDescription()).isEqualTo(description);
        assertThat(actual.get(0).getPrice()).isEqualTo(price);
        assertThat(actual.get(0).getCategory()).isEqualTo(savedCategory);
    }
    @Test
    void findByCategoryTestWillReturnEmptyList() {
        // Arrange
        Category category = new Category("elect");
        Category savedCategory = categoryRepo.save(category);
        String name = faker.book().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 3, 5);
        Product product = new Product(
                name,
                description,
                price,
                category
        );
        // Act
        underTest.save(product);
        List<Product> actual = underTest.findByCategory(savedCategory.getId()+1);
        // Assert
        assertThat(actual).hasSize(0);
    }
}