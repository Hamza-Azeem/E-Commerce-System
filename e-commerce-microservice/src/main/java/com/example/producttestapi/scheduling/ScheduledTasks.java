package com.example.producttestapi.scheduling;

import com.example.producttestapi.client.EmailClient;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.model.EmailRequest;
import com.example.producttestapi.service.CartService;
import com.example.producttestapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final CartService cartService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private final EmailClient emailClient;

    @Scheduled(fixedDelay = 7200000) // 2hours
    public void checkIfCartUsedInTwoHours()  {
        List<Cart> carts = cartService.findCartsOlderThanTwoHours();
        for(Cart cart : carts){
            User user = cart.getUser();
            user.setCart(null);
            userService.updateUserCart(user);
            cartService.deleteCartUsingSchedule(cart);
            logger.info("User {}'s cart has been deleted due to inactivity for 2 hours.", user.getFirstName()+" "+user.getLastName());
            EmailRequest emailRequest = EmailRequest.builder()
                    .to(user.getEmail())
                    .body("Your cart in springboot e-commerce application has been deleted due to inactivity for 2 hours." +
                            "Thanks for your understanding.")
                    .firstname(user.getFirstName())
                    .senderName("Developers Team")
                    .build();
            emailClient.sendEmail(emailRequest);
        }
    }
}
