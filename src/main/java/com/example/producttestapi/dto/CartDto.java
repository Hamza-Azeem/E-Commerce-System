package com.example.producttestapi.dto;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartDto {
    private double totalPrice;
    private int itemsCount;
    private List<CartItemDto> items = new ArrayList<>();
}
