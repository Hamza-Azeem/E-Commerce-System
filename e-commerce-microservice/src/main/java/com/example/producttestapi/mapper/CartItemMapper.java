package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CartItemDto;
import com.example.producttestapi.entities.CartItem;

public class CartItemMapper {
    public static CartItemDto convertToCartItemDto(CartItem cartItem) {
        return CartItemDto.builder()
                .productName(cartItem.getName())
                .pricePerItem(cartItem.getPricePerItem())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
