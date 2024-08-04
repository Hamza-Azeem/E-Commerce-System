package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashMap;
import java.util.Map;

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
    private double totalPrice = 0;
    private int totalItems = 0;
    @OneToMany(mappedBy = "cart", fetch = FetchType.EAGER, orphanRemoval = true)
    private Map<Integer, CartItem> items = new HashMap<>();
    @OneToOne(mappedBy = "cart")
    @JsonIgnore
    private User user;

    public Cart(User user) {
        this.user = user;
    }
    public void addItem(CartItem item) {
        totalItems += item.getQuantity();
        totalPrice += item.getPricePerItem() * item.getQuantity();
        items.put(item.getProduct().getId(), item);
    }
    public void removeItem(CartItem item) {
        totalPrice -= item.getPricePerItem() * item.getQuantity();
        totalItems -= item.getQuantity();
        items.remove(item.getProduct().getId());

    }
}
