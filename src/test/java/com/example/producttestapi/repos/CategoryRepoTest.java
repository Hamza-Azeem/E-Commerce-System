package com.example.producttestapi.repos;

import com.example.producttestapi.AbstractTestcontainers;
import com.example.producttestapi.entities.Category;
import com.github.javafaker.Cat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CategoryRepoTest extends AbstractTestcontainers {
    @Autowired
    private CategoryRepo underTest;

    @BeforeEach
    void setUp() {
        underTest.deleteAll();
    }

    @Test
    void findParentCategoryTest() {
        // Arrange
        String name = faker.book().genre();
        Category parentCategory = new Category(
                name
        );
        underTest.save(parentCategory);
        Category subCategory = new Category(
                faker.book().genre(),
                parentCategory,
                null
        );
        underTest.saveAll(Arrays.asList(parentCategory, subCategory));
        // Act
         List<Category> actual = underTest.findParentCategory();
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getName()).isEqualTo(parentCategory.getName());
    }

    @Test
    void findSubCategoriesOfParentCategoryTest() {
        // Arrange
        String name = faker.book().genre();
        Category parentCategory = new Category(
                name
        );
        underTest.save(parentCategory);
        Category subCategory = new Category(
                faker.book().genre(),
                parentCategory,
                null
        );
        Category result = underTest.save(parentCategory);
        underTest.save(subCategory);
        // Act
        List<Category> actual = underTest.findSubCategoriesOfParentCategory(
                result.getId()
        );
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(1);
        assertThat(actual.get(0).getName()).isEqualTo(subCategory.getName());
    }
}
