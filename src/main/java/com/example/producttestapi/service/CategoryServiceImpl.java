package com.example.producttestapi.service;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.CategoryRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static com.example.producttestapi.mapper.CategoryMapper.convertToCategoryDto;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    @Autowired
    public CategoryServiceImpl(CategoryRepo categoryRepo) {
        this.categoryRepo = categoryRepo;
    }

    @Override
    public List<CategoryDto> getAllCategory() {
        return categoryRepo.findAll()
                .stream().map(category -> convertToCategoryDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategory(int id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if (optionalCategory.isEmpty()) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return convertToCategoryDto(optionalCategory.get());
    }

    @Override
    public List<CategoryDto> getOnlyMainCategories() {
        return categoryRepo.findMainCategories()
                .stream().map(category -> convertToCategoryDto(category))
                .collect(Collectors.toList());
    }

    // Option 1
    @Override
    public List<CategoryDto> getAllSubCategoriesOfParentCategoryById(int id) {
        Optional<Category> optionalCategory = categoryRepo.findById(id);
        if (!optionalCategory.isPresent()) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        return optionalCategory.get().getSubCategories().stream()
                .map(category -> convertToCategoryDto(category))
                .collect(Collectors.toList());
    }

    @Override
    public void createCategory(Category category) {
        categoryRepo.save(category);
    }

    @Override
    public void deleteCategory(int id) {
        if (!categoryRepo.existsById(id)) {
            throw new ResourceNotFoundException("Category not found with id: " + id);
        }
        categoryRepo.deleteById(id);
    }

    @Override
    public  List<CategoryDto> GenerateCategoriesTree() {
        List<Category> categoryList = categoryRepo.findAll();
        Map<Integer, CategoryDto> categoryDtoMap = new HashMap<>();
        List<CategoryDto> result = new ArrayList<>();
        for (Category category : categoryList) {
            categoryDtoMap.put(category.getId(), convertToCategoryDto(category));
        }
        for(Category category: categoryList) {
            CategoryDto categoryDto = categoryDtoMap.get(category.getId());
            if(category.getParentCategory() != null){
                CategoryDto parentCategory = categoryDtoMap.get(category.getParentCategory().getId());
                parentCategory.getSubCategories().add(categoryDto);
            }else{
                result.add(categoryDto);
            }
        }
        return result;
    }


}