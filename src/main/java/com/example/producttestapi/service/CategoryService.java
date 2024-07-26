package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategory();
    CategoryDto getCategory(int id);
    List<CategoryDto> getOnlyMainCategories();
    List<CategoryDto> getAllSubCategoriesOfParentCategoryById(int id);
    void createCategory(Category category);

    void deleteCategory(int id);
    List<CategoryDto> findAllMainCategoriesAndTheirSubCategories();
}
