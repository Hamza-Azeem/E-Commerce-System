package com.example.producttestapi.service;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.InValidRequestException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.model.BuyingRequest;
import com.example.producttestapi.repos.CartRepo;
import jakarta.transaction.Transactional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import static com.example.producttestapi.mapper.CartMapper.convertToCartDto;

@Service
public class CartServiceImpl implements CartService{
    private final CartRepo cartRepo;
    private final UserService userService;
    private final CartItemService  cartItemService;
    private final ProductService productService;
    private final VoucherService voucherService;

    public CartServiceImpl(CartRepo cartRepo, UserService userService, UserService userService1, CartItemService cartItemService, ProductService productService, VoucherService voucherService) {
        this.cartRepo = cartRepo;
        this.userService = userService1;
        this.cartItemService = cartItemService;
        this.productService = productService;
        this.voucherService = voucherService;
    }

    @Override
    public void saveCart(Cart cart) {
        cartRepo.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Cart cart = findUserCart(userEmail);
        User user = userService.findUserByEmail(userEmail);
        for(CartItem cartItem: cart.getItems().values()){
            Product product = cartItem.getProduct();
            product.setQuantityInStore(product.getQuantityInStore() + cartItem.getQuantity());
            productService.updateProductWhenUsingCart(product);
        }
        user.setCart(null);
        userService.updateUserCart(user);
        cartRepo.delete(cart);
    }

    @Override
    @Transactional
    public CartDto deleteItemFromCart(long cartItemId, int count) {
        CartItem cartItem = cartItemService.findCartItemById(cartItemId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cart cart = findUserCart(authentication.getName());
        if(cart.getItems().containsKey(cartItem.getProduct().getId())){
            if(count <= 0 || count > cartItem.getQuantity()){
                throw new InValidRequestException("Quantity to remove is not valid.");
            }
            updateCartPriceAndQuantityWhenRemovingItem(cart, cartItem, count);
        }
        return convertToCartDto(cart);
    }

    @Override
    public CartDto findUserCart() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        return convertToCartDto(cartRepo.findCartByUserEmail(userEmail).orElseThrow(
                () -> new ResourceNotFoundException("You don't have a cart."))
        );
    }
    @Override
    public Cart findUserCart(String email) {
        return cartRepo.findCartByUserEmail(email).orElse(null);
    }

    @Transactional
    public CartDto addItemToCart(BuyingRequest buyingRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userEmail = authentication.getName();
        Product product = productService.getActualProductById(buyingRequest.getProductId());
        if(!isValidQuantity(product, buyingRequest.getCount())){
            throw new InValidRequestException("Quantity to take out of range.");
        }
        User user = userService.findUserByEmail(userEmail);
        Cart cart = findUserCart(userEmail);
        if (cart == null) {
            cart = new Cart(user);
            saveCart(cart);
        }
        double currentPrice = product.getPrice();
        voucherService.applyVoucherOnProduct(product);
        double priceAfterVoucher = product.getPrice();
        product.setPrice(currentPrice);
        CartItem cartItem = new CartItem(product, product.getName(), buyingRequest.getCount(), priceAfterVoucher, cart);
        if(cart.getItems().containsKey(product.getId())){
            updateCartPriceAndQuantityWhenAddingItem(cart, product, buyingRequest.getCount());
        }else{
            cartItemService.saveCartItem(cartItem);
            cart.addItem(cartItem);
        }
        updateProductQuantity(product, buyingRequest.getCount(), '-');
        saveCart(cart);
        user.setCart(cart);
        userService.updateUserCart(user);
        return convertToCartDto(cart);
    }
    private boolean isValidQuantity(Product product, int quantity){
        return quantity > 0 && quantity <= product.getQuantityInStore();
    }
    @Transactional
    protected void updateProductQuantity(Product product, int quantity, char operator) {
        if(operator == '-'){
            product.setQuantityInStore(product.getQuantityInStore() - quantity);
        }else {
            product.setQuantityInStore(product.getQuantityInStore() + quantity);
        }
        productService.updateProductWhenUsingCart(product);
    }
    @Transactional
    protected void updateCartPriceAndQuantityWhenAddingItem(Cart cart,Product product, int newQuantity) {
        CartItem oldCartItem = cart.getItems().get(product.getId());
        oldCartItem.setQuantity(newQuantity + oldCartItem.getQuantity());
        cart.setTotalItems(cart.getTotalItems() + newQuantity);
        cart.setTotalPrice(cart.getTotalPrice() + newQuantity * oldCartItem.getPricePerItem());
        cartItemService.updateCartItem(oldCartItem);
    }
    @Transactional
    protected void updateCartPriceAndQuantityWhenRemovingItem(Cart cart, CartItem cartItem, int count){
        if(count == cartItem.getQuantity()){
            cart.removeItem(cartItem);
            cartItemService.deleteCartItem(cartItem);
        }else{
            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPricePerItem() * count));
            cart.setTotalItems(cart.getTotalItems() - count);
            cartItem.setQuantity(cartItem.getQuantity() - count);
            cartItemService.saveCartItem(cartItem);
        }
        updateProductQuantity(cartItem.getProduct(), count, '+');
    }
}
