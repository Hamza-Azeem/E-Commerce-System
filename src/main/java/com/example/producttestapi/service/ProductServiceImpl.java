package com.example.producttestapi.service;

import com.example.producttestapi.dto.ProductDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;

import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.model.BuyingRequest;
import com.example.producttestapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.producttestapi.mapper.ProductMapper.convertToProductDto;

@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepo productRepo;
    private final VoucherService voucherService;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final CartService cartService;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, VoucherService voucherService, CategoryService categoryService, UserService userService, CartItemService cartItemService, CartService cartService) {
        this.productRepo = productRepo;
        this.voucherService = voucherService;
        this.categoryService = categoryService;
        this.userService = userService;
        this.cartItemService = cartItemService;
        this.cartService = cartService;
    }

    @Override
    @Cacheable(value = "products", key = "'all'")
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
    @Cacheable(value = "productId", key = "#id")
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
    @Cacheable(value = "products", key = "'category' + #categoryID")
    public List<ProductDto> getProductsByCategory(int categoryID) {
        categoryService.getCategory(categoryID);
        List<Product> products = productRepo.findByCategory(categoryID);
        for(Product product : products) {
            voucherService.applyVoucherOnProduct(product);
        }
        return products.stream().map(product -> convertToProductDto(product)).collect(Collectors.toList());
    }

    @Override
    @CacheEvict(value = "products", allEntries = true)
    public void createProduct(Product product) {
        productRepo.save(product);
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
    public Cart buyProduct(BuyingRequest buyingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Product product = productRepo.findById(buyingRequest.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + buyingRequest.getProductId()));
        User user = userService.findUserByEmail(userEmail);
        Cart cart = cartService.findUserCart(userEmail);
        if (cart == null) {
            cart = new Cart(user);
            cartService.saveCart(cart);
        }
        voucherService.applyVoucherOnProduct(product);
        CartItem cartItem = new CartItem(product, product.getName(), buyingRequest.getCount(), product.getPrice(), cart);
        cartItemService.saveCartItem(cartItem);
        cart.addItem(cartItem);
        cart.calculateTotalPrice();
        cartService.saveCart(cart);
        user.setCart(cart);
        userService.updateUserCart(user);
        return cart;
    }
}
