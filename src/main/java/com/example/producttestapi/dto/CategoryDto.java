package com.example.producttestapi.dto;

import com.example.producttestapi.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class CategoryDto {
    private String name;
    private Set<Category> subCategories;
}
