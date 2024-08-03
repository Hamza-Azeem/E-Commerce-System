package com.example.producttestapi.service;

import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.repos.CartItemRepo;
import org.springframework.stereotype.Service;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepo cartItemRepo;

    public CartItemServiceImpl(CartItemRepo cartItemRepo) {
        this.cartItemRepo = cartItemRepo;
    }

    @Override
    public void saveCartItem(CartItem cartItem) {
        cartItemRepo.save(cartItem);
    }

    @Override
    public void deleteCartItem(CartItem cartItem) {
        cartItemRepo.delete(cartItem);
    }
}
