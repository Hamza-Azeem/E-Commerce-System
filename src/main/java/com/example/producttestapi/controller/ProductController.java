package com.example.producttestapi.controller;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;

import com.example.producttestapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @Operation(
            description = "GET endpoint to retrieve ProductDto list",
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
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }
    @Operation(
            description = "GET endpoint to retrieve ProductDto by product ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @Operation(
            description = "GET endpoint to retrieve all products (productDto) in a category by category ID",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
                    ,@ApiResponse(
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable("categoryID") int categoryID) {
        return ResponseEntity.ok().body(productService.getProductsByCategory(categoryID));
    }
    @Operation(
            description = "POST endpoint to create a new product",
            responses = {
                    @ApiResponse(
                            description = "created",
                            responseCode = "201"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
                    ,@ApiResponse(
                    description = "unauthorized",
                    responseCode = "403"
            )
            }
    )
    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "PUT endpoint to update an existing product by product",
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200"
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
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable int id, @RequestBody ProductDto productDto) {
        return ResponseEntity.ok().body(productService.updateProduct(id, productDto));
    }
    @Operation(
            description = "Delete endpoint to delete an existing product by product ID",
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
                            description = "Not found",
                            responseCode = "404"
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.notFound().build();
    }
}
