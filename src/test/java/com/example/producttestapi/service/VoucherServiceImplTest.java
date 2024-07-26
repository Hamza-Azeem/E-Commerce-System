package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.VoucherRepo;
import com.github.javafaker.Faker;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VoucherServiceImplTest {
    @Mock
    private VoucherRepo voucherRepo;
    @InjectMocks
    private VoucherServiceImpl underTest;
    private Faker faker = new Faker();
    @Test
    void findAllVouchersTest() {
        // Act
        underTest.findAllVouchers();
        // Assert
        verify(voucherRepo).findAll();
    }

    @Test
    void findVoucherByIdTest() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        when(voucherRepo.findById(id)).thenReturn(Optional.of(voucher));
        // Act
        Voucher actual = underTest.findVoucherById(id);
        // Assert
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getCode()).isEqualTo(code);
        assertThat(actual.getDiscount()).isEqualTo(discount);
        assertThat(actual.getExpireDate()).isEqualTo(expireDate);
    }
    @Test
    void findVoucherByIdWillThrowExceptionWhenVoucherNotFound() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        // Assert
        assertThatThrownBy(()-> underTest.findVoucherById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage(String.format("Voucher not found with this id: %s", id));
    }

    @Test
    void createVoucherTest() {
        // Arrange
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                code,
                discount,
                expireDate
        );
        // Act
        underTest.createVoucher(voucher);
        // Assert
        verify(voucherRepo).save(voucher);
    }

    @Test
    void findVoucherByCodeTest() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        when(voucherRepo.findByCode(code)).thenReturn(Optional.of(voucher));
        // Act
        Voucher actual = underTest.findVoucherByCode(code);
        // Assert
        assertThat(actual.getId()).isEqualTo(id);
        assertThat(actual.getCode()).isEqualTo(code);
        assertThat(actual.getDiscount()).isEqualTo(discount);
        assertThat(actual.getExpireDate()).isEqualTo(expireDate);
    }
    @Test
    void findVoucherByCodeWillThrowExceptionWhenVoucherNotFound() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        // Assert
        assertThatThrownBy(()-> underTest.findVoucherByCode(code))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Voucher not found with this code : " + voucher.getCode());
    }

    @Test
    void deleteVoucherTest() {
        // Arrange
        long id = faker.number().randomNumber();
        when(voucherRepo.existsById(id)).thenReturn(true);
        // Act
        underTest.deleteVoucher(id);
        // Assert
        verify(voucherRepo).deleteById(id);
    }

    @Test
    void deleteVoucherWillThrowExceptionWhenVoucherNotFound() {
        // Arrange
        long id = faker.number().randomNumber();
        when(voucherRepo.existsById(id)).thenReturn(false);
        // Act
        // Assert
        assertThatThrownBy(()-> underTest.deleteVoucher(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Voucher not found with id: " + id);
    }

    @Test
    void applyVoucherOnProductTest() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        String productName = faker.name().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 2, 4);
        Product product = new Product(
                productName,
                description,
                price,
                null,
                voucher
        );
        when(voucherRepo.existsById(id)).thenReturn(true);
        BigDecimal priceAfterDiscount = BigDecimal.valueOf(price).subtract(BigDecimal.valueOf(price).multiply(discount.divide(BigDecimal.valueOf(100))));
        // Act
        underTest.applyVoucherOnProduct(product);
        // Assert
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualTo(priceAfterDiscount.doubleValue());
        assertThat(product.getVoucherCode().getId()).isEqualTo(id);
    }
    @Test
    void applyVoucherOnProductWillDoNothingWhenVoucherIsNull() {
        // Arrange
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        String productName = faker.name().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 2, 4);
        Product product = new Product(
                productName,
                description,
                price,
                null,
                null
        );
        // Act
        underTest.applyVoucherOnProduct(product);
        // Assert
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualTo(price);
        assertThat(product.getVoucherCode()).isEqualTo(null);
    }
    @Test
    void applyVoucherOnProductWillThrowExceptionWhenVoucherNotFound() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        String productName = faker.name().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 2, 4);
        Product product = new Product(
                productName,
                description,
                price,
                null,
                voucher
        );
        when(voucherRepo.existsById(id)).thenReturn(false);
        // Act
        // Assert
        assertThatThrownBy(() -> underTest.applyVoucherOnProduct(product))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Voucher not found with this code : " + id);
    }
    @Test
    void applyVoucherOnProductWillDeleteVoucherWhenExpired() {
        // Arrange
        long id = faker.number().randomNumber();
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomNumber());
        LocalDate expireDate = LocalDate.now().minusDays(1);
        Voucher voucher = new Voucher(
                id,
                code,
                discount,
                expireDate
        );
        String productName = faker.name().title();
        String description = faker.gameOfThrones().quote();
        double price = faker.number().randomDouble(2, 2, 4);
        Product product = new Product(
                productName,
                description,
                price,
                null,
                voucher
        );
        when(voucherRepo.existsById(id)).thenReturn(true);
        // Act
        underTest.applyVoucherOnProduct(product);
        // Assert
        assertThat(product.getName()).isEqualTo(productName);
        assertThat(product.getDescription()).isEqualTo(description);
        assertThat(product.getPrice()).isEqualTo(price);
    }

}