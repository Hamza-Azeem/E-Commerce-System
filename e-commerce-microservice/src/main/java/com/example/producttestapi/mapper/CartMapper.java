package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;

import java.util.stream.Collectors;

public class CartMapper {

    public static CartDto convertToCartDto(Cart cart) {
        return CartDto.builder()
                .totalPrice(cart.getTotalPrice())
                .items(cart.getItems().values()
                        .stream()
                        .map(CartItemMapper::convertToCartItemDto)
                        .collect(Collectors.toList()))
                .itemsCount(cart.getTotalItems())
                .build();
    }
}
