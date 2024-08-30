package com.example.producttestapi.service;


import com.example.producttestapi.entities.CartItem;


public interface CartItemService {
    void saveCartItem(CartItem cartItem);
    void deleteCartItem(CartItem cartItem);
    CartItem findCartItemById(Long id);
    CartItem updateCartItem(CartItem cartItem);
}
