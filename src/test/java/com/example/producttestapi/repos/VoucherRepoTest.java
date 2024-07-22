package com.example.producttestapi.repos;

import com.example.producttestapi.AbstractTestcontainers;
import com.example.producttestapi.entities.Voucher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class VoucherRepoTest extends AbstractTestcontainers {

    @Autowired
    private VoucherRepo underTest;

    @Test
    void findByCode() {
        // Arrange
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomDigit());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                code,
                discount,
                expireDate
                );
        underTest.save(voucher);
        // Act
        Optional<Voucher> actual = underTest.findByCode(code);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.get().getId()).isGreaterThan(0);
        assertThat(actual.get().getCode()).isEqualTo(code);
        assertThat(actual.get().getDiscount()).isEqualTo(discount);
        assertThat(actual.get().getExpireDate()).isEqualTo(expireDate);
    }

    @Test
    void findById() {
        // Arrange
        String code = faker.code().asin();
        BigDecimal discount = BigDecimal.valueOf(faker.number().randomDigit());
        LocalDate expireDate = LocalDate.now().plusDays(1);
        Voucher voucher = new Voucher(
                code,
                discount,
                expireDate
        );
        underTest.save(voucher);
        long id = underTest.findAll()
                .stream()
                .filter(v -> v.getCode().equals(code))
                .map(Voucher::getId)
                .findFirst().orElseThrow();
        // Act
        Optional<Voucher> actual = underTest.findById(id);
        // Assert
        assertThat(actual).isNotNull();
        assertThat(actual.get().getId()).isEqualTo(id);
        assertThat(actual.get().getCode()).isEqualTo(code);
        assertThat(actual.get().getDiscount()).isEqualTo(discount);
        assertThat(actual.get().getExpireDate()).isEqualTo(expireDate);
    }
}