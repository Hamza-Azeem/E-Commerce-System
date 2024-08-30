package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
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
    @UpdateTimestamp
    private LocalDateTime updatedDate;
    @OneToMany(mappedBy = "cart", fetch = FetchType.LAZY, orphanRemoval = true)
    private Map<Integer, CartItem> items = new HashMap<>();
    @OneToOne(mappedBy = "cart", fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    public Cart(User user) {
        this.user = user;
    }
    public void addItem(CartItem item) {
        totalItems += item.getQuantity();
        totalPrice += roundToTwoDecimal(item.getPricePerItem() * item.getQuantity());
        items.put(item.getProduct().getId(), item);
    }
    public void removeItem(CartItem item) {
        totalPrice -= roundToTwoDecimal(item.getPricePerItem() * item.getQuantity());
        totalItems -= item.getQuantity();
        items.remove(item.getProduct().getId());

    }
    private double roundToTwoDecimal(double price){
        BigDecimal priceBigDecimal = new BigDecimal(price);
        return priceBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
}
