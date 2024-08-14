package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.model.BuyingRequest;

import java.util.List;

public interface ProductService {
    List<ProductDto> getAllProducts();
    ProductDto getProductById(int id);
    List<ProductDto> getProductsByCategory(int categoryID);
    void createProduct(ProductDto productDto);
    ProductDto updateProduct(int id, ProductDto productDto);
    void deleteProduct(int id);
    Product getActualProductById(int id);
    void updateProductWhenUsingCart(Product product);
}
