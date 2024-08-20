package com.example.producttestapi.dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class VoucherDto {
    @NotEmpty(message = "Voucher code is required")
    private String code;
    @NotEmpty(message = "Voucher discount amount is required")
    private BigDecimal discount;
    @NotNull(message = "Expiry date is required")
    private LocalDate expiryDate;
}
