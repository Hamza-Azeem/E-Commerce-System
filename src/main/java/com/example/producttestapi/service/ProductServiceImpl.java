package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.producttestapi.mapper.ProductMapper.convertToProductDto;

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
    @Cacheable(value = "allProducts")
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepo.findAll();
        for(Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        List<ProductDto> result = products.stream()
                .map(p -> convertToProductDto(p)).collect(Collectors.toList());
        return result;
    }

    @Override
    @Cacheable(value = "productById", key = "#id")
    public ProductDto getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        voucherService.applyVoucherOnProduct(product);
        return convertToProductDto(product);
    }

    @Override
    @Cacheable(value = "productByCategory", key = "#categoryID")
    public List<ProductDto> getProductsByCategory(int categoryID) {
        categoryService.getCategory(categoryID);
        List<Product> products = productRepo.findByCategory(categoryID);
        for(Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products.stream().map(product -> convertToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "productByCategory", key = "#product.category.id")
            }
    )
    public void createProduct(Product product) {
        productRepo.save(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "productByCategory", allEntries = true)
            },
            put = {
                    @CachePut(value = "productById", key = "#product.id"),
            }
    )
    public ProductDto updateProduct(Product product) {
        if (!productRepo.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        productRepo.save(product);
        voucherService.applyVoucherOnProduct(product);
        return convertToProductDto(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "allProducts", allEntries = true),
                    @CacheEvict(value = "productById", key = "#id"),
                    @CacheEvict(value = "productByCategory", allEntries = true)
            }
    )
    public void deleteProduct(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        productRepo.deleteById(id);
    }
}
