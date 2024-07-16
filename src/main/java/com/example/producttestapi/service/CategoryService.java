package com.example.producttestapi.service;

import com.example.producttestapi.entities.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryService {
    List<Category> getAllCategory();
    Optional<Category> getCategory(int id);
    List<Category> getOnlyMainCategories();
    void createCategory(Category category);

    void deleteCategory(int id);
}
