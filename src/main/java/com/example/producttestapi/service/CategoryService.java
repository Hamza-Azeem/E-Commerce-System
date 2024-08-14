package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;

import java.util.List;
import java.util.Map;

public interface CategoryService {
    List<CategoryDto> getAllCategory();
    CategoryDto getCategory(int id);
    List<CategoryDto> getOnlyMainCategories();
    List<CategoryDto> getAllSubCategoriesOfParentCategoryById(int id);
    void createCategory(Category category);
    Category getCategoryByName(String name);
    void deleteCategory(int id);
    List<CategoryDto> GenerateCategoriesTree();
}
