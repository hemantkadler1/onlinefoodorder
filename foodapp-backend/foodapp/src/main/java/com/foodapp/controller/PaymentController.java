package com.foodapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodapp.service.PaymentService;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/pay/{orderId}")
    public ResponseEntity<String> payForOrder(@PathVariable Long orderId,
            @RequestParam String method) {
        boolean success = paymentService.processPayment(orderId, method);

        if (success) {
            return ResponseEntity.ok("Payment successful for order ID: " + orderId);
        } else {
            return ResponseEntity.status(500).body("Payment failed for order ID: " + orderId);
        }
    }
}
