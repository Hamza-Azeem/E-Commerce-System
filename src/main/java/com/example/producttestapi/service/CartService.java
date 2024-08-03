package com.example.producttestapi.service;

import com.example.producttestapi.entities.Cart;

public interface CartService {
    public void saveCart(Cart cart);
    public Cart findUserCart(String email);
}
