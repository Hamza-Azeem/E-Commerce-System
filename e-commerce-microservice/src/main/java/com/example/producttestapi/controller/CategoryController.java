package com.example.producttestapi.controller;

import com.example.producttestapi.dto.CategoryDto;
import com.example.producttestapi.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Operation(
            description = "GET endpoint to retrieve categoryDto list",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<?>> getAllCategories() {
        return ResponseEntity.ok().body(categoryService.getAllCategory());
    }

    @Operation(
            description = "GET endpoint to retrieve categoryDto by category ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(categoryService.getCategory(id));
    }
    @Operation(
            description = "GET endpoint to retrieve categoryDto list consists of only main categories",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping("/main")
    public ResponseEntity<?> getAllMainCategories(){
        return ResponseEntity.ok().body(categoryService.getOnlyMainCategories());
    }

    @Operation(
            description = "GET endpoint to retrieve categoryDto list consists of only sub-categories of the category ID provided",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping("/{id}/sub")
    public ResponseEntity<?> getAllSubCategoriesOfParentById(@PathVariable int id){
        return ResponseEntity.ok().body(categoryService.getAllSubCategoriesOfParentCategoryById(id));
    }

    @Operation(
            description = "GET endpoint to retrieve categoryDto list presented in a tree form",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping("/tree")
    public ResponseEntity<?> getCategoriesTree(){
        return ResponseEntity.ok().body(categoryService.GenerateCategoriesTree());
    }

    @Operation(
            description = "POST endpoint to create a new category",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "unauthorized",
                            responseCode = "403"
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "DELETE endpoint to delete an existing category by category ID",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "404"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "unauthorized",
                            responseCode = "403"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable("id") int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.notFound().build();
    }
}