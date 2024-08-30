package com.e_commerce.email_microservice;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEmail {
    private String to;
    private String from;
    private String subject;
    private String body;
    private String firstname;
    private String senderName;

}
