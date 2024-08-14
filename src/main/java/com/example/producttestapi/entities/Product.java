package com.example.producttestapi.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;
    private String name;
    private String description;
    private double price;
    private int quantityInStore;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id")
    private Category category ;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "voucher_id")
    private Voucher voucherCode;
    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<CartItem> item;
    public Product() {
    }
    public Product(String name, String description, double price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
    public Product(String name, String description, double price, Category category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
    }

    public Product(String name, String description, double price, Voucher voucherCode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.voucherCode = voucherCode;
    }



    public Product(String name, String description, double price, Category category, Voucher voucherCode) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.voucherCode = voucherCode;
    }

    public Product(int id, String name, String description, double price, Category category, Voucher voucherCode) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.voucherCode = voucherCode;
    }
}
