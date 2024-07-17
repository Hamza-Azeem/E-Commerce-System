package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String code;
    private BigDecimal discount;
    private LocalDate expireDate;
    @OneToMany(mappedBy = "voucherCode", cascade = CascadeType.DETACH)
    @JsonIgnore
    private Set<Product> products;

    public void addProduct(Product product){
        if(products == null){
            products = new HashSet<>();
        }
        products.add(product);
    }
}
