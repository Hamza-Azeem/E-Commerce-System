package com.example.producttestapi.service;

import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.Voucher;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, VoucherService voucherService) {
        this.productRepo = productRepo;
        this.voucherService = voucherService;
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> products = productRepo.findAll();
        products.forEach(this::applyVoucherDiscount);
        return products;
    }

    @Override
    public Product getProductById(int id) {
        Optional<Product> optionalProduct = productRepo.findById(id);
        if (!optionalProduct.isPresent()) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        Product product = optionalProduct.get();
        applyVoucherDiscount(product);
        return product;
    }

    @Override
    public List<Product> getProductsByCategory(int categoryID) {
        List<Product> products = productRepo.findByCategory(categoryID);
        products.forEach(this::applyVoucherDiscount);
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
    private void applyVoucherDiscount(Product product) {
        if (product.getVoucherCode() != null) {
            Voucher voucher = voucherService.findVoucherById(product.getVoucherCode().getId());
            if (voucher != null) {
                BigDecimal discount = voucher.getDiscount();
                BigDecimal productPrice = BigDecimal.valueOf(product.getPrice());
                // price - (price * (discount/100))
                BigDecimal discountedPrice = productPrice.subtract(productPrice.multiply(discount.divide(BigDecimal.valueOf(100))));
                product.setPrice(discountedPrice.doubleValue());
            }
        }
    }
}
