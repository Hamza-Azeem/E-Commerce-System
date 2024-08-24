package com.example.producttestapi.scheduling;

import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.service.CartService;
import com.example.producttestapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final CartService cartService;
    private final UserService userService;

    @Scheduled(fixedDelay = 7200000) // 2hours
    public void checkIfCartUsedInTwoHours(){
        List<Cart> carts = cartService.findCartsOlderThanTwoHours();
        for(Cart cart : carts){
            User user = cart.getUser();
            user.setCart(null);
            userService.updateUserCart(user);
            cartService.deleteCartUsingSchedule(cart);
        }
    }
}
