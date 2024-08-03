package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {
    @Query("SELECT p FROM Product p WHERE p.category.id =:categoryID")
    List<Product> findByCategory(int categoryID);
}
