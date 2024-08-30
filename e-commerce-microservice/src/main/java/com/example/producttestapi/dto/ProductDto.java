package com.example.producttestapi.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    @NotEmpty(message = "Product name is required")
    private String name;
    @NotEmpty(message = "Product description is required")
    private String description;
    @NotNull
    @Min(value = 0, message = "Product price can't be less than 0")
    private double price;
    @NotNull(message = "Product quantity is required")
    @Min(value = 0, message = "Product price can't be less than 0")
    private int quantityInStore;
    @NotEmpty(message = "Product category is required")
    private String categoryName;
    private String voucherCode;
}
