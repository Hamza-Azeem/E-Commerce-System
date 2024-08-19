package com.example.producttestapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class VoucherDto {
    private String code;
    private BigDecimal discount;
    private LocalDate expiryDate;
}
