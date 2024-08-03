package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "cart_item")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE,
            CascadeType.REFRESH,
            CascadeType.DETACH,
            CascadeType.PERSIST})
    @JoinColumn(name = "product_id")
    @JsonIgnore
    private Product product;
    private int quantity;
    private double pricePerItem;
    @ManyToOne
    @JoinColumn(name = "cart_id")
    @JsonIgnore
    private Cart cart;

    public CartItem(Product product, String name,int quantity, double pricePerItem) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
    }
    public CartItem(Product product, String name,int quantity, double pricePerItem, Cart cart) {
        this.product = product;
        this.name = name;
        this.quantity = quantity;
        this.pricePerItem = pricePerItem;
        this.cart = cart;
    }
}
