package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.ProductRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
}