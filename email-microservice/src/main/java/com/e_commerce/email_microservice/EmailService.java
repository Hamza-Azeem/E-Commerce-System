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

    public void sendHtmlEmail(NotificationEmail notificationEmail) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        if(notificationEmail.getFrom() != null && !notificationEmail.getFrom().isEmpty()) {
            message.setFrom(fromEmailAddress);
        }else {
            message.setFrom(new InternetAddress(fromEmailAddress));
        }
        message.setRecipients(MimeMessage.RecipientType.TO, notificationEmail.getTo());
        String htmlTemplate = readFile();
        String htmlContent = htmlTemplate.replace("${name}", notificationEmail.getFirstname());
        htmlContent = htmlContent.replace("${message}", notificationEmail.getBody());
        htmlContent = htmlContent.replace("${senderName}", notificationEmail.getSenderName());
        message.setContent(htmlContent, "text/html;charset=utf-8");
        mailSender.send(message);
        logger.info("Email sent to {}", notificationEmail.getTo());
    }

    private String readFile() throws IOException {
        Path path = Paths.get("D:\\spring-boot\\e-commerce\\email-microservice\\email-microservice\\src\\main\\resources\\templates\\emailTemplate.html");
        return Files.readString(path, StandardCharsets.UTF_8);
    }

}
