package com.foodapp.service;

public interface PaymentService {

    boolean processPayment(Long orderId, String paymentMethod);
}
