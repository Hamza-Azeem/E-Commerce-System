package com.example.producttestapi.model;

import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EmailRequest {
    private String to;
    private String from;
    private String subject;
    private String body;
    private String firstname;
    private String senderName;
}
