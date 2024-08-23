package com.example.producttestapi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductSearch {
    private String productName;
    private String categoryName;
    private double minimumPrice;
    private double maximumPrice;
}
