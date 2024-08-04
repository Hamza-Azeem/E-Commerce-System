package com.example.producttestapi.controller;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.model.BuyingRequest;
import com.example.producttestapi.model.RemovingRequest;
import com.example.producttestapi.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }
    @GetMapping
    public ResponseEntity<CartDto> getCart() {
        return ResponseEntity.ok(cartService.findUserCart());
    }
    @PostMapping("/add-item")
    public ResponseEntity<?> addProductToCart(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(cartService.addItemToCart(buyingRequest));
    }
    @DeleteMapping()
    public ResponseEntity<?> deleteCart(){
        cartService.deleteCart();
        return ResponseEntity.notFound().build();
    }
    @PutMapping("/delete-item")
    public ResponseEntity<?> deleteItemFromCart(@RequestBody RemovingRequest removingRequest){
        CartDto cartDto = cartService.deleteItemFromCart(removingRequest.getCartItemId(), removingRequest.getCount());
        return ResponseEntity.ok().body(cartDto);
    }

}
