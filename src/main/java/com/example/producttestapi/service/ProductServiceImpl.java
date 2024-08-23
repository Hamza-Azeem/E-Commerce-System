package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Category;
import com.example.producttestapi.entities.Product;

import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.mapper.ProductMapper;
import com.example.producttestapi.repos.ProductRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.producttestapi.mapper.ProductMapper.convertToProduct;
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
    @Cacheable(value = "products", key = "#pageNum + #pageSize + #sortBy")
    public List<ProductDto> getAllProducts(int pageNum, int pageSize, String sortBy) {
        Pageable page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        List<Product> products = productRepo.findAll(page).getContent();
        for (Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products.stream()
                .map(ProductMapper::convertToProductDto).collect(Collectors.toList());
    }

    @Override
    @Cacheable(value = "productId", key = "#id")
    public ProductDto getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        voucherService.applyVoucherOnProduct(product);
        return convertToProductDto(product);
    }

    @Override
    @Cacheable(value = "products", key = "#categoryID + #pageNum + #pageSize + #sortBy")
    public List<ProductDto> getProductsByCategory(int categoryID, int pageNum, int pageSize, String sortBy) {
        categoryService.getCategory(categoryID);
        Pageable page = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        List<Product> products = productRepo.findByCategory(categoryID, page);
        for (Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products.stream().map(ProductMapper::convertToProductDto).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void createProduct(ProductDto productDto) {
        Category category = categoryService.getCategoryByName(productDto.getCategoryName());
        Product product = convertToProduct(productDto);
        product.setCategory(category);
        if (productDto.getVoucherCode() != null) {
            Voucher voucher = voucherService.findVoucherByCode(productDto.getVoucherCode());
            product.setVoucherCode(voucher);
        }
        productRepo.save(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
                    @CacheEvict(value = "productId", key = "#id")
            }
    )
    @Transactional
    public ProductDto updateProduct(int id, ProductDto productDto) {
        Product product = getActualProductById(id);
        Category category = categoryService.getCategoryByName(productDto.getCategoryName());
        product.setCategory(category);
        product.setName(productDto.getName());
        product.setPrice(productDto.getPrice());
        product.setDescription(productDto.getDescription());
        product.setQuantityInStore(productDto.getQuantityInStore());
        if (productDto.getVoucherCode() != null) {
            Voucher voucher = voucherService.findVoucherByCode(productDto.getVoucherCode());
            product.setVoucherCode(voucher);
        }
        productRepo.save(product);
        return convertToProductDto(product);
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
                    @CacheEvict(value = "productId", key = "#id"),
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

    @Override
    public Product getActualProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        return optionalProduct.get();
    }

    @Override
    @Caching(
            evict = {
                    @CacheEvict(value = "products", allEntries = true),
            },
            put = {
                    @CachePut(value = "productId", key = "#product.id"),
            }
    )
    public void updateProductWhenUsingCart(Product product) {
        if (!productRepo.existsById(product.getId())) {
            throw new ResourceNotFoundException("Product not found with id: " + product.getId());
        }
        productRepo.save(product);
    }
}
