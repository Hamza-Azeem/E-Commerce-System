package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;

public class ProductMapper {
    public static ProductDto convertToProductDto(Product product) {
        return ProductDto.builder()
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .quantityInStore(product.getQuantityInStore())
                .categoryName(product.getCategory().getName() != null ? product.getCategory().getName() : "No category")
                .voucherCode(product.getVoucherCode() != null ? product.getVoucherCode().getCode() : "Not on sale")
                .build();
    }
    public static Product convertToProduct(ProductDto productDto) {
        return Product.builder()
                .name(productDto.getName())
                .description(productDto.getDescription())
                .price(productDto.getPrice())
                .quantityInStore(productDto.getQuantityInStore())
                .build();
    }
}
