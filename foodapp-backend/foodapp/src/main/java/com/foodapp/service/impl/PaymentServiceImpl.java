package com.foodapp.service.impl;

import com.foodapp.model.Order;
import com.foodapp.repository.OrderRepository;
import com.foodapp.service.PaymentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public boolean processPayment(Long orderId, String method) {
        try {
            Order order = orderRepository.findById(orderId).orElse(null);
            if (order == null) {
                return false;
            }

            order.setStatus("PAID");
            orderRepository.save(order);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
