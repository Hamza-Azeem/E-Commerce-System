package com.example.producttestapi.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
public class BuyingRequest {
    private int productId;
    private int count;
    public BuyingRequest(int productId, String userEmail) {
        this.productId = productId;
        count = 1;
    }
}
