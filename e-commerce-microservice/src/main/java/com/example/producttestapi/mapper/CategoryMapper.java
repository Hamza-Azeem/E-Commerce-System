package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class CategoryMapper {
    public static CategoryDto convertToCategoryDto(Category category){
        return CategoryDto.builder()
                .name(category.getName())
                .subCategories(new ArrayList<>())
                .build();
    }

    public static Category convertToCategory(CategoryDto categoryDto){
        return Category.builder()
                .name(categoryDto.getName())
                .build();
    }
}
