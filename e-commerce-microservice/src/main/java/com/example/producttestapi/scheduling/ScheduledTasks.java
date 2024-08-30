package com.example.producttestapi.scheduling;

import com.example.producttestapi.email.EmailService;
import com.example.producttestapi.entities.Cart;
import com.example.producttestapi.entities.User;
import com.example.producttestapi.service.CartService;
import com.example.producttestapi.service.UserService;
import com.example.producttestapi.service.impl.CartServiceImpl;
import com.mysql.cj.log.Log;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {
    private final CartService cartService;
    private final UserService userService;
    private final EmailService emailService;
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    @Scheduled(fixedDelay = 7200000) // 2hours
    public void checkIfCartUsedInTwoHours() throws MessagingException, IOException {
        List<Cart> carts = cartService.findCartsOlderThanTwoHours();
        for(Cart cart : carts){
            User user = cart.getUser();
            user.setCart(null);
            userService.updateUserCart(user);
            cartService.deleteCartUsingSchedule(cart);
            String userName = user.getFirstName() + " " + user.getLastName();
            String text = "Your cart in springboot e-commerce application has been deleted due to inactivity for 2 hours." +
                    "Thanks for your understanding.";
            logger.info("User {}'s cart has been deleted due to inactivity for 2 hours.", userName);
            emailService.sendHtmlEmail(user.getEmail(), userName, text);
        }
    }
}
