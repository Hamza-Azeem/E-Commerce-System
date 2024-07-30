package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;

public class ProductMapper {
    public static ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
