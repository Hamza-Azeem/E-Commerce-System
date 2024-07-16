package com.example.producttestapi.mapper;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;

public class CategoryMapper {
    public static CategoryDto convertToCategoryDto(Category category){
        return CategoryDto.builder()
                .name(category.getName())
                .build();
    }
}
