package com.e_commerce.email_microservice;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationEmail {
    private String to;
    private String firstname;

}
