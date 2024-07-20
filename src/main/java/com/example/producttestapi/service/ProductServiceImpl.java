package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;
    private final CategoryService categoryService;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, VoucherService voucherService, CategoryService categoryService) {
        this.productRepo = productRepo;
        this.voucherService = voucherService;
        this.categoryService = categoryService;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        for(Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products;
    }

    @Override
    public Product getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        voucherService.applyVoucherOnProduct(product);
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryID) {
        categoryService.getCategory(categoryID);
        List<Product> products = productRepo.findByCategory(categoryID);
        for(Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products;
    }

    @Override
    public Product createProduct(Product product) {
        return productRepo.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        if (!productRepo.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        return productRepo.save(product);
    }

    @Override
    public void deleteProduct(int id) {
        if (!productRepo.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepo.deleteById(id);
    }
}
