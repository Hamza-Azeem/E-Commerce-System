package com.e_commerce.email_microservice;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender mailSender;
    private final Logger logger = LoggerFactory.getLogger(EmailService.class);
    @Value("${spring.mail.username}")
    private String fromEmailAddress;
    private final String  CART_EMAIL_PATH = "D:\\study\\spring-boot\\e-commerce\\email-microservice\\src\\main\\resources\\templates\\cartDeletionEmail.html";
    private final String  REGISTRATION_EMAIL_PATH = "D:\\study\\spring-boot\\e-commerce\\email-microservice\\src\\main\\resources\\templates\\successfulRegistrationEmail.html";

    public void sendHtmlEmail(NotificationEmail notificationEmail, String emailTemplatePath) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(fromEmailAddress));
        message.setRecipients(MimeMessage.RecipientType.TO, notificationEmail.getTo());
        String htmlTemplate = readFile(emailTemplatePath);
        String htmlContent = htmlTemplate.replace("${name}", notificationEmail.getFirstname());
        message.setContent(htmlContent, "text/html;charset=utf-8");
        mailSender.send(message);
        logger.info("Email sent to {}", notificationEmail.getTo());

    }
    public void sendHtmlCartEmail(NotificationEmail notificationEmail) throws MessagingException, IOException {
       sendHtmlEmail(notificationEmail, CART_EMAIL_PATH);
    }
    public void sendHtmlRegistrationEmail(NotificationEmail notificationEmail) throws MessagingException, IOException {
        sendHtmlEmail(notificationEmail, REGISTRATION_EMAIL_PATH);
    }

    private String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }

}
