package com.example.producttestapi.service.impl;

import com.example.producttestapi.dto.CartDto;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.CartItem;
import com.example.producttestapi.entities.Product;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.exception.InValidRequestException;
import com.example.producttestapi.exception.ResourceNotFoundException;
import com.example.producttestapi.model.BuyingRequest;
import com.example.producttestapi.repos.CartRepo;
import com.example.producttestapi.service.*;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.example.producttestapi.mapper.CartMapper.convertToCartDto;

@Service
public class CartServiceImpl implements CartService {
    private final CartRepo cartRepo;
    private final UserService userService;
    private final CartItemService cartItemService;
    private final ProductService productService;
    private final VoucherService voucherService;
    private final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    public CartServiceImpl(CartRepo cartRepo, UserService userService, CartItemService cartItemService, ProductService productService, VoucherService voucherService) {
        this.cartRepo = cartRepo;
        this.userService = userService;
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
        logger.info("User [{}] delete his cart", userEmail);
    }

    @Override
    @Transactional
    public CartDto deleteItemFromCart(long cartItemId, int count) {
        CartItem cartItem = cartItemService.findCartItemById(cartItemId);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Cart cart = findUserCart(authentication.getName());
        if(cart.getItems().containsKey(cartItem.getProduct().getId())){
            if(count <= 0 || count > cartItem.getQuantity()){
                logger.warn("User: [{}] tried to remove quantity= [{}] of cartItem: [{}] while he only has quantity= [{}]", authentication.getName(), count, cartItem.getId(), cartItem.getQuantity());
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
        Optional<Cart> cartOptional = cartRepo.findCartByUserEmail(userEmail);
        if(cartOptional.isEmpty()){
            throw new ResourceNotFoundException("You don't have a cart!");
        }
        return convertToCartDto(cartOptional.get());
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
        User user = userService.findUserByEmail(userEmail);
        Cart cart = initializeUserCart(user);

        voucherService.applyVoucherOnProduct(product);
        double priceAfterVoucher = roundToTwoDecimal(product.getPrice());

        if (cart.getItems().containsKey(product.getId())) {
            CartItem existingCartItem = cart.getItems().get(product.getId());
            addQuantityToExistingCartItem(userEmail, buyingRequest, product, cart, existingCartItem);
        } else {
            addNewCartItemToCart(userEmail, buyingRequest, product, cart, priceAfterVoucher);
        }

        updateUserAndCart(user, cart);
        return convertToCartDto(cart);
    }

    private Cart initializeUserCart(User user) {
        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart(user);
            saveCart(cart);
            logger.info("User [{}] created a new cart", user.getEmail());
        }
        return cart;
    }

    private void addQuantityToExistingCartItem(String userEmail, BuyingRequest buyingRequest, Product product, Cart cart, CartItem existingCartItem) {
        int newQuantity = existingCartItem.getQuantity() + buyingRequest.getCount();
        if (!isValidQuantity(product, newQuantity)) {
            logInvalidQuantity(userEmail, buyingRequest, product);
            throw new InValidRequestException("Quantity to take out of range.");
        }
        updateCartPriceAndQuantityWhenAddingItem(cart, existingCartItem, buyingRequest.getCount());
    }

    private void addNewCartItemToCart(String userEmail, BuyingRequest buyingRequest, Product product, Cart cart, double priceAfterVoucher) {
        if (!isValidQuantity(product, buyingRequest.getCount())) {
            logInvalidQuantity(userEmail, buyingRequest, product);
            throw new InValidRequestException("Quantity to take out of range.");
        }
        CartItem cartItem = new CartItem(product, product.getName(), buyingRequest.getCount(), priceAfterVoucher, cart);
        cartItemService.saveCartItem(cartItem);
        cart.addItem(cartItem);
        logCartItemAdded(cartItem.getId(), buyingRequest.getCount(), cart.getId());
    }

    private void logInvalidQuantity(String userEmail, BuyingRequest buyingRequest, Product product) {
        logger.warn("User: [{}] tried to add quantity= [{}] of product: [{}] while product has only quantity of= [{}]",
                userEmail, buyingRequest.getCount(), buyingRequest.getProductId(), product.getQuantityInStore());
    }
    private void logCartItemAdded(long cartItemId, int quantity, long cartId){
        logger.info("Cart item: [{}] with quantity = [{}] added to cart with id: [{}]", cartItemId, quantity, cartId);
    }

    private void updateUserAndCart(User user, Cart cart) {
        saveCart(cart);
        user.setCart(cart);
        userService.updateUserCart(user);
    }

    @Override
    public List<Cart> findCartsOlderThanTwoHours() {
        LocalDateTime time = LocalDateTime.now().minusHours(2);
        return cartRepo.findCartsOlderThanTwoHours(time);
    }

    @Override
    public void deleteCartUsingSchedule(Cart cart) {
        cartRepo.delete(cart);
    }

    private boolean isValidQuantity(Product product, int quantity){
        return quantity > 0 && quantity <= product.getQuantityInStore();
    }

    private void updateCartPriceAndQuantityWhenAddingItem(Cart cart, CartItem oldCartItem, int newQuantity) {
        oldCartItem.setQuantity(newQuantity + oldCartItem.getQuantity());
        cart.setTotalItems(cart.getTotalItems() + newQuantity);
        cart.setTotalPrice(cart.getTotalPrice() + newQuantity * oldCartItem.getPricePerItem());
        cartItemService.updateCartItem(oldCartItem);
        logCartItemAdded(oldCartItem.getId(), newQuantity, cart.getId());
    }

    private void updateCartPriceAndQuantityWhenRemovingItem(Cart cart, CartItem cartItem, int count){
        if(count == cartItem.getQuantity()){
            cart.removeItem(cartItem);
            cartItemService.deleteCartItem(cartItem);
        }else{
            double currentTotalPrice = cart.getTotalPrice();
            double itemTotalPrice = cartItem.getPricePerItem() * count;
            double newTotalPrice = roundToTwoDecimal(currentTotalPrice - itemTotalPrice);
            cart.setTotalPrice(newTotalPrice);
//            cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getPricePerItem() * count));
            cart.setTotalItems(cart.getTotalItems() - count);
            cartItem.setQuantity(cartItem.getQuantity() - count);
            cartItemService.saveCartItem(cartItem);
        }
        logger.info("Cart item: [{}] with quantity = [{}] removed from cart with id: [{}]", cartItem.getId(), count, cartItem.getCart().getId());
    }

    private double roundToTwoDecimal(double price){
        BigDecimal priceBigDecimal = new BigDecimal(price);
        return priceBigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue();
    }
    // unneeded feature
    // Product quantity doesn't have to be changed when a user adds an item of this product to cart.
    // It will only be changed when customer place an order.
//    @Transactional
//    protected void updateProductQuantity(Product product, int quantity, char operator) {
//        if(operator == '-'){
//            product.setQuantityInStore(product.getQuantityInStore() - quantity);
//        }else {
//            product.setQuantityInStore(product.getQuantityInStore() + quantity);
//        }
//        productService.updateProductWhenUsingCart(product);
//    }

}
