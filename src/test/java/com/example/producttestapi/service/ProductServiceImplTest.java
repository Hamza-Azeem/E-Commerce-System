package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.repos.ProductRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
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
        long id = faker.number().randomNumber();
        Voucher voucher = new Voucher(
                id,
                "discount20",
                BigDecimal.valueOf(20),
                LocalDate.now().plusDays(1)
        );
        Product product = new Product(
                "name",
                "description",
                100,
                voucher
        );
        Product product2 = new Product(
                "name2",
                "description2",
                200,
                voucher
        );
        List<Product> products = Arrays.asList(product, product2);
        when(productRepo.findAll()).thenReturn(products);
        when(voucherService.findVoucherById(id)).thenReturn(voucher);
        // Act
        List<Product> actual = underTest.getAllProducts();
        // Assert
        verify(productRepo).findAll();
        assertThat(actual).hasSize(2);
        assertThat(actual.get(0).getName()).isEqualTo(product.getName());
        assertThat(actual.get(0).getDescription()).isEqualTo(product.getDescription());
        assertThat(actual.get(0).getPrice()).isEqualTo(80);
        assertThat(actual.get(1).getName()).isEqualTo(product2.getName());
        assertThat(actual.get(1).getDescription()).isEqualTo(product2.getDescription());
        assertThat(actual.get(1).getPrice()).isEqualTo(160);

    }
}