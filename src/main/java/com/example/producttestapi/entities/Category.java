package com.example.producttestapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Setter
@Getter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int id ;
    private String name;
    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Product> products;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;
    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.LAZY)
    private Set<Category> subCategories;

    public Category() {

    }
    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name, Category parentCategory, Set<Category> subCategories) {
        this.name = name;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public Category(int id, String name, List<Product> products, Category parentCategory, Set<Category> subCategories) {
        this.id = id;
        this.name = name;
        this.products = products;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
    }

    public void addProduct(Product product){
        if(products == null){
            products = new ArrayList<>();
        }
        products.add(product);
    }
    public void addSubCategory(Category category){
        if(subCategories == null){
            subCategories = new HashSet<>();
        }
        subCategories.add(category);
        category.setParentCategory(this);
    }
}
