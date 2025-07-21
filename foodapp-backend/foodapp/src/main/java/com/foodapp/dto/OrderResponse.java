package com.foodapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private String status;
    private double totalPrice;
}
