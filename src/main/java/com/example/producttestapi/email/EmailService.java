package com.example.producttestapi.email;

import com.example.producttestapi.scheduling.ScheduledTasks;
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
    private final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);

    private String file;
    @Value("${spring.mail.username}")
    private String fromEmailAddress;

    public void sendHtmlEmail(String to, String userName, String text) throws MessagingException, IOException {
        MimeMessage message = mailSender.createMimeMessage();
        message.setFrom(new InternetAddress(fromEmailAddress));
        message.setRecipients(MimeMessage.RecipientType.TO, to);

        String htmlTemplate = readFile();
        String htmlContent = htmlTemplate.replace("${name}", userName);
        htmlContent = htmlContent.replace("${message}", text);
        message.setContent(htmlContent, "text/html;charset=utf-8");
        mailSender.send(message);
        logger.info("Email sent to {}", to);
    }
    private String readFile() throws IOException {
        Path path = Paths.get("D:\\spring-boot\\e-commerce\\src\\main\\resources\\emailTemplate.html");
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
