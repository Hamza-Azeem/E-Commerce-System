package com.example.producttestapi.service;


import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;

public interface CartItemService {
    void saveCartItem(CartItem cartItem);
    void deleteCartItem(CartItem cartItem);
}
