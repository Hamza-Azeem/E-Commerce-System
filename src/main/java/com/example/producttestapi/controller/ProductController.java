package com.example.producttestapi.controller;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.model.BuyingRequest;
import com.example.producttestapi.service.ProductService;
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

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllProducts() {
        return ResponseEntity.ok().body(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProduct(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(productService.getProductById(id));
    }

    @GetMapping("/category/{categoryID}")
    public ResponseEntity<List<ProductDto>> getProductsByCategory(@PathVariable("categoryID") int categoryID) {
        return ResponseEntity.ok().body(productService.getProductsByCategory(categoryID));
    }

    @PostMapping
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        productService.createProduct(product);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    @PostMapping("/buy")
    public ResponseEntity<?> buyProduct(@RequestBody BuyingRequest buyingRequest){
        return ResponseEntity.ok(productService.buyProduct(buyingRequest));
    }

    @PutMapping
    public ResponseEntity<ProductDto> updateProduct(@RequestBody Product product) {
        return ResponseEntity.ok().body(productService.updateProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable("id") int id) {
        productService.deleteProduct(id);
        return ResponseEntity.notFound().build();
    }
}
