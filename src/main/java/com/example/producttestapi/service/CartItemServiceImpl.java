package com.example.producttestapi.service;


import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CartItemRepo;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    @Override
    public CartItem findCartItemById(Long id) {
        Optional<CartItem> cartItem = cartItemRepo.findById(id);
        if(cartItem.isPresent()) {
            return cartItem.get();
        }
        throw new ResourceNotFoundException("CartItem with id " + id + " not found");
    }

    @Override
    public CartItem updateCartItem(CartItem cartItem) {
        return cartItemRepo.save(cartItem);
    }
}
