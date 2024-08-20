package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.DuplicateResourceException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CategoryRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
    void getAllMainCategoriesTest(){
        // Arrange
        // Act
        underTest.getOnlyMainCategories();
        // Assert
        verify(categoryRepo).findMainCategories();
    }

    @Test
    void createCategoryTest() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("name", null, null);
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepo.findByName("name")).thenReturn(Optional.empty());
        // Act
        underTest.createCategory(categoryDto);
        // Assert
        verify(categoryRepo).save(categoryArgumentCaptor.capture());
        Category actual = categoryArgumentCaptor.getValue();
        assertThat(actual.getName()).isEqualTo(categoryDto.getName());
        assertThat(actual).isNotNull();
    }
    @Test
    void createCategoryWillThrowExceptionWhenCategoryWithSameNameExistsTest() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("name", null, null);
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepo.findByName("name")).thenReturn(Optional.of(Category.builder().name("name").build()));
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.createCategory(categoryDto))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Category name already exists");
    }
    @Test
    void createCategoryWithHavingParentCategoryTest() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("name", null, "parent-category");
        Category parent = Category.builder().name("parent-category").build();
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepo.findByName("name")).thenReturn(Optional.empty());
        when(categoryRepo.findByName("parent-category")).thenReturn(Optional.of(parent));
        // Act
        underTest.createCategory(categoryDto);
        // Assert
        verify(categoryRepo).save(categoryArgumentCaptor.capture());
        Category actual = categoryArgumentCaptor.getValue();
        assertThat(actual.getName()).isEqualTo(categoryDto.getName());
        assertThat(actual.getParentCategory()).isEqualTo(parent);
    }
    @Test
    void createCategoryWillThrowExceptionWhenParentNameIsInvalidTest() {
        // Arrange
        CategoryDto categoryDto = new CategoryDto("name", null, "parent-category");
        ArgumentCaptor<Category> categoryArgumentCaptor = ArgumentCaptor.forClass(Category.class);
        when(categoryRepo.findByName("name")).thenReturn(Optional.empty());
        when(categoryRepo.findByName("parent-category")).thenReturn(Optional.empty());
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.createCategory(categoryDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Category not found with name: " + "parent-category");
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