package com.example.producttestapi.client;

import com.example.producttestapi.model.EmailRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "email-microservice", url = "${spring.config.email-url}")
public interface EmailClient {
    @PostMapping("/cart/send")
    void sendCartEmail(@RequestBody EmailRequest emailRequest);
    @PostMapping("/successful/registration/send")
    void sendRegistrationEmail(@RequestBody EmailRequest emailRequest);
}
