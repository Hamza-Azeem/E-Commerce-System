package com.example.producttestapi.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class RemovingRequest {
    private long cartItemId;
    private int count;
}
