package com.example.producttestapi.controller;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;

import com.example.producttestapi.model.ProductSearch;
import com.example.producttestapi.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
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
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
            }
    )
    @GetMapping
    public ResponseEntity<?> getAllProducts(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok().body(productService.getAllProducts(pageNum, pageSize, sortBy));
    }
    @Operation(
            description = "GET endpoint to retrieve ProductDto by product ID",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    ),
                    @ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }
    @GetMapping("/search")
    public ResponseEntity<?> searchProducts(
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "") String productName,
            @RequestParam(defaultValue = "") String categoryName,
            @RequestParam(defaultValue = "-1") double minimumPrice,
            @RequestParam(defaultValue = "0") double maximumPrice
            ){
        return ResponseEntity.ok().body(productService.searchProducts(
                new ProductSearch(productName, categoryName, minimumPrice, maximumPrice), pageNum, pageSize, sortBy
        ));
    }

    @Operation(
            description = "GET endpoint to retrieve all products (productDto) in a category by category ID",
            responses = {
                    @ApiResponse(
                            description = "success",
                            responseCode = "200"
                    ),
                    @ApiResponse(
                            description = "unauthenticated",
                            responseCode = "401"
                    )
                    ,@ApiResponse(
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(
            @PathVariable("categoryID") int categoryID,
            @RequestParam(defaultValue = "0") int pageNum,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok().body(productService.getProductsByCategory(categoryID, pageNum, pageSize, sortBy));
    }
    @Operation(
            description = "POST endpoint to create a new product",
            responses = {
                    @ApiResponse(
                            description = "success",
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
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(
            description = "PUT endpoint to update an existing product by product",
            responses = {
                    @ApiResponse(
                            description = "success",
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
                            description = "not found",
                            responseCode = "404"
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable int id, @Valid @RequestBody ProductDto productDto) {
        return ResponseEntity.ok().body(productService.updateProduct(id, productDto));
    }
    @Operation(
            description = "DELETE endpoint to delete an existing product by product ID",
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
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.notFound().build();
    }
}
