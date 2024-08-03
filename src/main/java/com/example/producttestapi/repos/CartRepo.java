package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CartRepo extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c WHERE c.user.email=:email")
    Optional<Cart> findCartByUserEmail(String email);
}
