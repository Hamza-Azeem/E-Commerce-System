package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CategoryRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    private CategoryServiceImpl underTest;
    @Mock
    private CategoryRepo categoryRepo;
    private Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        underTest = new CategoryServiceImpl(categoryRepo);
    }

    @Test
    void getAllCategoryTest() {
        // Arrange
        Category category1 = new Category(1, "name1");
        Category category2 = new Category(2, "name2");
        List<Category> categoryList = Arrays.asList(
                category1,
                category2
                );
        when(categoryRepo.findAll()).thenReturn(categoryList);
        // Act
        List<CategoryDto> actual = underTest.getAllCategory();
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getName()).isEqualTo(category1.getName());
        assertThat(actual.get(1).getName()).isEqualTo(category2.getName());
    }

    @Test
    void getCategoryWillReturnCategoryDto() {
        // Arrange
        int id = faker.number().randomDigit();
        String name = faker.name().firstName();
        Category category = new Category(id, name);
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
        // Act
        CategoryDto actual = underTest.getCategory(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.getName()).isEqualTo(name);
    }
    @Test
    void getCategoryWillThrowExceptionWhenCategoryNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        // Assert
        assertThatThrownBy(() -> underTest.getCategory(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: " + id);
    }

    @Test
    void getOnlyMainCategoriesTest() {
        // Arrange
        // Act
        underTest.getOnlyMainCategories();
        // Assert
        verify(categoryRepo).findMainCategories();

    }

    @Test
    void getAllSubCategoriesOfParentCategoryByIdWillReturnSubCategories() {
        // Arrange
        int id = faker.number().randomDigit();
        String name = faker.name().firstName();
        String name1 = faker.name().firstName();
        String name2 = faker.name().firstName();
        Set<Category> subCategories = Set.of(
                new Category(2, name1),
                new Category(3, name2)
        );
        Category category = new Category(id,
                name,
                null,
                null,
                subCategories
        );
        when(categoryRepo.findById(id)).thenReturn(Optional.of(category));
        // Act
        List<CategoryDto> actual = underTest.getAllSubCategoriesOfParentCategoryById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getName()).isEqualTo(name1);
        assertThat(actual.get(1).getName()).isEqualTo(name2);
    }

    @Test
    void getAllSubCategoriesOfParentCategoryByIdWillThrowExceptionWhenCategoryNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        // Act
        // Assert
        assertThatThrownBy(() -> underTest
                .getAllSubCategoriesOfParentCategoryById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: " + id);
    }
    @Test
    void findAllMainCategoriesAndTheirSubCategoriesTest(){
        // Todo[
        //    {
        //        "name": "Electronics",
        //        "subCategories": [
        //            {
        //                "id": 12,
        //                "name": "Laptops",
        //                "subCategories": [
        //                    {
        //                        "id": 21,
        //                        "name": "Gaming Laptops",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 22,
        //                        "name": "Ultrabooks",
        //                        "subCategories": []
        //                    }
        //                ]
        //            },
        //            {
        //                "id": 2,
        //                "name": "Mobile Phones",
        //                "subCategories": [
        //                    {
        //                        "id": 19,
        //                        "name": "Android Phones",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 20,
        //                        "name": "iPhones",
        //                        "subCategories": []
        //                    }
        //                ]
        //            }
        //        ]
        //    },
        //    {
        //        "name": "Home Appliances",
        //        "subCategories": [
        //            {
        //                "id": 14,
        //                "name": "Laundry Appliances",
        //                "subCategories": [
        //                    {
        //                        "id": 26,
        //                        "name": "Dryers",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 25,
        //                        "name": "Washing Machines",
        //                        "subCategories": []
        //                    }
        //                ]
        //            },
        //            {
        //                "id": 13,
        //                "name": "Kitchen Appliances",
        //                "subCategories": [
        //                    {
        //                        "id": 23,
        //                        "name": "Refrigerators",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 24,
        //                        "name": "Microwaves",
        //                        "subCategories": []
        //                    }
        //                ]
        //            }
        //        ]
        //    },
        //    {
        //        "name": "Clothing",
        //        "subCategories": [
        //            {
        //                "id": 16,
        //                "name": "Women's Wear",
        //                "subCategories": [
        //                    {
        //                        "id": 30,
        //                        "name": "Skirts",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 29,
        //                        "name": "Dresses",
        //                        "subCategories": []
        //                    }
        //                ]
        //            },
        //            {
        //                "id": 15,
        //                "name": "Men's Wear",
        //                "subCategories": [
        //                    {
        //                        "id": 27,
        //                        "name": "T-Shirts",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 28,
        //                        "name": "Jeans",
        //                        "subCategories": []
        //                    }
        //                ]
        //            }
        //        ]
        //    },
        //    {
        //        "name": "Books",
        //        "subCategories": [
        //            {
        //                "id": 18,
        //                "name": "Non-Fiction",
        //                "subCategories": [
        //                    {
        //                        "id": 33,
        //                        "name": "Biographies",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 34,
        //                        "name": "Self-Help",
        //                        "subCategories": []
        //                    }
        //                ]
        //            },
        //            {
        //                "id": 17,
        //                "name": "Fiction",
        //                "subCategories": [
        //                    {
        //                        "id": 31,
        //                        "name": "Science Fiction",
        //                        "subCategories": []
        //                    },
        //                    {
        //                        "id": 32,
        //                        "name": "Fantasy",
        //                        "subCategories": []
        //                    }
        //                ]
        //            }
        //        ]
        //    }
        //]
    }

    @Test
    void createCategoryTest() {
        // Arrange
        Category category = new Category("name");
        // Act
        underTest.createCategory(category);
        // Assert
        verify(categoryRepo).save(category);
    }

    @Test
    void deleteCategoryTest() {
        // Arrange
        int id = faker.number().randomDigit();
        when(categoryRepo.existsById(id)).thenReturn(true);
        // Act
        underTest.deleteCategory(id);
        // Assert
        verify(categoryRepo).deleteById(id);
    }

    @Test
    void deleteCategoryWillThrowExceptionWhenCategoryNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        when(categoryRepo.existsById(id)).thenReturn(false);
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.deleteCategory(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with id: " + id);
    }
}