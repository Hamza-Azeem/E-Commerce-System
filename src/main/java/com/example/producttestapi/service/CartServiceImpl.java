package com.example.producttestapi.service;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.repos.CartRepo;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepo cartRepo;

    public CartServiceImpl(CartRepo cartRepo) {
        this.cartRepo = cartRepo;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    public Cart findUserCart(String email) {
        Cart cart = cartRepo.findCartByUserEmail(email).orElse(null);
        return cart;
    }
}
