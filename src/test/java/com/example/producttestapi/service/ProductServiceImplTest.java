package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    private ProductServiceImpl underTest;
    @Mock
    private CategoryService categoryService;
    @Mock
    private ProductRepo productRepo;
    @Mock
    private VoucherService voucherService;
    private Faker faker = new Faker();
    @BeforeEach
    void setUp() {
        underTest = new ProductServiceImpl(productRepo, voucherService, categoryService);
    }

    @Test
    void getAllProductsTest() {
        // Arrange
        Product product = new Product(
                "product1",
                "description",
                25
        );
        Product product2 = new Product(
                "product2",
                "description2",
                252
        );
        List<Product> products = Arrays.asList(product, product2);
        // Act
        when(productRepo.findAll()).thenReturn(products);
        underTest.getAllProducts();
        // Assert
        verify(productRepo).findAll();
        verify(voucherService).applyVoucherOnProduct(product);
        verify(voucherService).applyVoucherOnProduct(product2);
    }

    @Test
    void getProductByIdWillReturnProduct() {
        // Arrange
        int id = faker.number().randomDigit();
        String name = faker.name().firstName();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 2, 4);
        Product product = new Product(
                id,
                name,
                description,
                price,
                null,
                null
        );
        when(productRepo.findById(id)).thenReturn(Optional.of(product));
        // Act
        Product actual = underTest.getProductById(id);
        // Assert
        verify(voucherService).applyVoucherOnProduct(product);
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice()).isEqualTo(price);
        assertThat(actual.getCategory()).isNull();
        assertThat(actual.getVoucherCode()).isNull();
    }
    @Test
    void getProductByIdWillThrowExceptionWhenProductNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        // Assert
        assertThatThrownBy(() -> underTest.getProductById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + id);
    }
    @Test
    void getAllProductsByCategoryTest() {
        // Arrange
        int categoryId = faker.number().randomDigit();
        Product product = new Product(
                "product1",
                "description",
                25
        );
        Product product2 = new Product(
                "product2",
                "description2",
                252
        );
        List<Product> products = Arrays.asList(product, product2);
        // Act
        when(productRepo.findByCategory(categoryId)).thenReturn(products);
        underTest.getProductsByCategory(categoryId);
        // Assert
        verify(productRepo).findByCategory(categoryId);
        verify(voucherService).applyVoucherOnProduct(product);
        verify(voucherService).applyVoucherOnProduct(product2);
    }
    @Test
    void getAllProductsByCategoryWilLThrowExceptionWhenCategoryNotFound() {
        // Arrange
        int categoryId = faker.number().randomDigit();
        when(categoryService.getCategory(categoryId))
                .thenThrow(ResourceNotFoundException.class);
        // Assert
        assertThatThrownBy(() -> underTest.getProductsByCategory(categoryId))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createProductTest() {
        // Arrange
        Product product = new Product("product1",
                faker.name().firstName(),
                faker.number().randomDigit());
        // Act
        underTest.createProduct(product);
        // Assert
        verify(productRepo).save(product);
    }

    @Test
    void updateProductTest() {
        // Arrange
        int id = faker.number().randomDigit();
        Product product = new Product(
                id,
                "product1",
                faker.name().firstName(),
                faker.number().randomDigit(),
                null,
                null);
        when(productRepo.existsById(id)).thenReturn(true);
        // Act
        underTest.updateProduct(product);
        // Assert
        verify(productRepo).save(product);
    }
    @Test
    void updateProductWillThrowExceptionWhenProductNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        Product product = new Product(
                id,
                "product1",
                faker.name().firstName(),
                faker.number().randomDigit(),
                null,
                null);
        when(productRepo.existsById(id)).thenReturn(false);
        // Assert
        assertThatThrownBy(()->underTest.updateProduct(product))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + id);
    }
    @Test
    void deleteProductTest() {
        // Arrange
        int id = faker.number().randomDigit();
        when(productRepo.existsById(id)).thenReturn(true);
        // Act
        underTest.deleteProduct(id);
        // Assert
        verify(productRepo).deleteById(id);
    }
    @Test
    void deleteProductWillThrowExceptionWhenProductNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        when(productRepo.existsById(id)).thenReturn(false);
        // Assert
        assertThatThrownBy(()->underTest.deleteProduct(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + id);
    }

}