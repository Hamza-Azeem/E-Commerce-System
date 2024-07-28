package com.example.producttestapi.controller;

import com.example.producttestapi.entities.Category;
import com.example.producttestapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<?>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategory());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(categoryService.getCategory(id));
    }
    @GetMapping("/main")
    public ResponseEntity<?> getAllMainCategories(){
        return ResponseEntity.ok().body(categoryService.getOnlyMainCategories());
    }
    @GetMapping("/{id}/sub")
    public ResponseEntity<?> getAllSubCategoriesOfParentById(@PathVariable int id){
        return ResponseEntity.ok().body(categoryService.getAllSubCategoriesOfParentCategoryById(id));
    }
    @GetMapping("/tree")
    public ResponseEntity<?> getCategoriesTree(){
        return ResponseEntity.ok().body(categoryService.GenerateCategoriesTree());
    }

    @PostMapping
    public ResponseEntity<?> createCategory(@RequestBody Category category) {
        categoryService.createCategory(category);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.notFound().build();
    }
}