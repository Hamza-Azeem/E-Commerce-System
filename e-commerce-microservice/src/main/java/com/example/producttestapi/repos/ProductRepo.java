package com.example.producttestapi.repos;

import com.example.producttestapi.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


import java.util.List;


public interface ProductRepo extends JpaRepository<Product,Integer>, JpaSpecificationExecutor<Product> {
    @Query("SELECT p FROM Product p WHERE p.category.id =:categoryID")
    List<Product> findByCategory(int categoryID);
    @Query("SELECT p FROM Product p WHERE p.category.id =:categoryID")
    List<Product> findByCategory(int categoryID, Pageable pageable);
}
