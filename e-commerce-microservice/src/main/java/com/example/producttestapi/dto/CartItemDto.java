package com.example.producttestapi.dto;

import lombok.*;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItemDto {
    private String productName;
    private int quantity;
    private double pricePerItem;
}
