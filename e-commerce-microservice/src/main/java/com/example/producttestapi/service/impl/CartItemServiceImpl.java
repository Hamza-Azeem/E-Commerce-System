package com.example.producttestapi.service.impl;


import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CartItemRepo;
import com.example.producttestapi.service.CartItemService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepo cartItemRepo;
    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

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
