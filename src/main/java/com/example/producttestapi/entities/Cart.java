package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "total_price")
    private double totalPrice;
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
    private List<CartItem> items;
    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private User user;

    public Cart(User user) {
        this.user = user;
    }

    public void addItem(CartItem item) {
        if(items == null) {
            items = new ArrayList<CartItem>();
        }
        items.add(item);
    }
    public void calculateTotalPrice() {
        totalPrice = 0;
        for(CartItem item : items) {
            double itemsPrice = item.getPricePerItem() * item.getQuantity();
            totalPrice += itemsPrice;
        }
    }
}
