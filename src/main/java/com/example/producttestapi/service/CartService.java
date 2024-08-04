package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.model.BuyingRequest;

public interface CartService {
    public void saveCart(Cart cart);
    public Cart findUserCart(String email);
    public CartDto findUserCart();
    public void deleteCart();
    public CartDto deleteItemFromCart(long cartItemId, int count);
    public CartDto addItemToCart(BuyingRequest buyingRequest);
}
