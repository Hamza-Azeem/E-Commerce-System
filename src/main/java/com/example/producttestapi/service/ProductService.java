package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(int id);
    List<ProductDto> getProductsByCategory(int categoryID);
    void createProduct(Product product);
    ProductDto updateProduct(Product product);
    void deleteProduct(int id);

}
