package com.example.producttestapi.dto;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDto {
    private String name;
    private String description;
    private double price;
    private int quantityInStore;
    private String categoryName;
    private String voucherCode;
}
