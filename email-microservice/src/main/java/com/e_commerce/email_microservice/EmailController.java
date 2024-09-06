package com.e_commerce.email_microservice;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {
    private final EmailService emailService;

    @PostMapping("/cart/send")
    public void sendCartEmail(@RequestBody NotificationEmail notificationEmail) throws MessagingException, IOException {
        emailService.sendHtmlCartEmail(notificationEmail);
    }@PostMapping("/successful/registration/send")
    public void sendSuccessfulRegistrationEmail(@RequestBody NotificationEmail notificationEmail) throws MessagingException, IOException {
        emailService.sendHtmlRegistrationEmail(notificationEmail);
    }
}
