package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;


import java.math.BigDecimal;
import java.time.LocalDate;
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
        Product product = Product.builder()
                .name("product1")
                .description("description1")
                .price(251)
                .category(new Category("Test"))
                .build();
        Product product2 = Product.builder()
                .name("product2")
                .description("description2")
                .price(252)
                .category(new Category("Test"))
                .build();
        List<Product> products = Arrays.asList(product, product2);
        Pageable page = PageRequest.of(0,2, Sort.by("id"));
        Page<Product> productPage = new PageImpl<>(products, page, products.size());
        when(productRepo.findAll(page)).thenReturn(productPage);
        // Act
        underTest.getAllProducts(0,2,"id");
        // Assert
        verify(productRepo).findAll(page);
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
        int categoryId = faker.number().randomDigit();
        Category category = new Category(categoryId,"TEST");
        Product product = new Product(
                id,
                name,
                description,
                price,
                category,
                null
        );
        when(productRepo.findById(id)).thenReturn(Optional.of(product));
        // Act
        ProductDto actual = underTest.getProductById(id);
        // Assert
        verify(voucherService).applyVoucherOnProduct(product);
        assertThat(actual.getName()).isEqualTo(name);
        assertThat(actual.getDescription()).isEqualTo(description);
        assertThat(actual.getPrice()).isEqualTo(price);
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
        Category category = new Category(categoryId,"TEST");
        Product product = Product.builder()
                .category(category)
                .build();
        Product product2 = Product.builder()
                .category(category)
                .build();
        List<Product> products = Arrays.asList(product, product2);
        Pageable pageable = PageRequest.of(0, 2, Sort.by("id"));
        when(productRepo.findByCategory(categoryId, pageable)).thenReturn(products);
        // Act
        underTest.getProductsByCategory(categoryId, 0, 2, "id");
        // Assert
        verify(productRepo).findByCategory(categoryId, pageable);
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
        assertThatThrownBy(() -> underTest.getProductsByCategory(categoryId, 0, 2, "id"))
                .isInstanceOf(ResourceNotFoundException.class);
    }
    @Test
    void createProductTestWithOutVoucher() {
        // Arrange
        ProductDto productDto = ProductDto.builder()
                .name(faker.name().title())
                .description(faker.lorem().word())
                .categoryName("Test")
                .price(2000)
                .quantityInStore(15)
                .build();
        Category category = new Category("Test");
        when(categoryService.getCategoryByName("Test")).thenReturn(category);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        // Act
        underTest.createProduct(productDto);
        // Assert
        verify(productRepo).save(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getName()).isEqualTo(productDto.getName());
        assertThat(actual.getCategory().getName()).isEqualTo(productDto.getCategoryName());
        assertThat(actual.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(actual.getPrice()).isEqualTo(productDto.getPrice());
        assertThat(actual.getQuantityInStore()).isEqualTo(productDto.getQuantityInStore());
    }
    @Test
    void createProductTestWithVoucher() {
        // Arrange
        String discount15 = "DISCOUNT15";
        ProductDto productDto = ProductDto.builder()
                .name(faker.name().title())
                .description(faker.lorem().word())
                .categoryName("Test")
                .price(2000)
                .quantityInStore(15)
                .voucherCode(discount15)
                .build();
        Category category = new Category("Test");
        Voucher voucher = new Voucher(discount15, BigDecimal.valueOf(15), LocalDate.now().plusDays(1));
        when(categoryService.getCategoryByName("Test")).thenReturn(category);
        when(voucherService.findVoucherByCode(discount15)).thenReturn(voucher);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        // Act
        underTest.createProduct(productDto);
        // Assert
        verify(productRepo).save(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getName()).isEqualTo(productDto.getName());
        assertThat(actual.getCategory().getName()).isEqualTo(productDto.getCategoryName());
        assertThat(actual.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(actual.getVoucherCode().getCode()).isEqualTo(productDto.getVoucherCode());
        assertThat(actual.getPrice()).isEqualTo(productDto.getPrice());
    }

    @Test
    void updateProductTest() {
        // Arrange
        int id = faker.number().randomDigit();
        ProductDto productDto = ProductDto.builder()
                .name(faker.name().title())
                .description(faker.lorem().word())
                .categoryName("Test")
                .price(2000)
                .build();
        Category category = new Category("Test");
        Product product = new Product(
                faker.name().title(),
                faker.lorem().word(),
                faker.random().nextDouble(),
                category
        );
        when(productRepo.findById(id)).thenReturn(Optional.of(product));
        when(categoryService.getCategoryByName("Test")).thenReturn(category);
        ArgumentCaptor<Product> productArgumentCaptor = ArgumentCaptor.forClass(Product.class);
        // Act
        underTest.updateProduct(id, productDto);
        // Assert
        verify(productRepo).save(productArgumentCaptor.capture());
        Product actual = productArgumentCaptor.getValue();
        assertThat(actual.getName()).isEqualTo(productDto.getName());
        assertThat(actual.getCategory().getName()).isEqualTo(productDto.getCategoryName());
        assertThat(actual.getDescription()).isEqualTo(productDto.getDescription());
        assertThat(actual.getPrice()).isEqualTo(productDto.getPrice());
    }
    @Test
    void updateProductWillThrowExceptionWhenProductNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        ProductDto productDto = ProductDto.builder().build();
        when(productRepo.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.updateProduct(id, productDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + id);
    }
    @Test
    void deleteProductTest() {
        // Arrange
        int id = faker.number().randomDigit();
        Product product = new Product(
                id,
                "product1",
                faker.name().firstName(),
                faker.number().randomDigit(),
                null,
                null);
        when(productRepo.findById(id)).thenReturn(Optional.of(product));
        // Act
        underTest.deleteProduct(id);
        // Assert
        verify(productRepo).deleteById(id);
    }
    @Test
    void deleteProductWillThrowExceptionWhenProductNotFound() {
        // Arrange
        int id = faker.number().randomDigit();
        when(productRepo.findById(id)).thenReturn(Optional.empty());
        // Assert
        assertThatThrownBy(()->underTest.deleteProduct(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Product not found with id: " + id);
    }

}