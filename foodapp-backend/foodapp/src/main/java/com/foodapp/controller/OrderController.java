package com.foodapp.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.foodapp.dto.OrderRequest;
import com.foodapp.dto.OrderResponse;
import com.foodapp.model.Order;
import com.foodapp.security.JwtUtil;
import com.foodapp.service.OrderService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/place")
    public ResponseEntity<OrderResponse> placeOrder(@RequestBody OrderRequest orderRequest) {
        Order order = orderService.placeOrder(orderRequest);
        OrderResponse response = new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public List<Order> getOrders(@RequestParam String email) {
        return orderService.getOrdersByEmail(email);
    }

    @PutMapping("/pay/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public ResponseEntity<?> simulatePayment(@PathVariable Long orderId) {
        Order paidOrder = orderService.updateOrderStatus(orderId, "PAID");
        return ResponseEntity.ok(Map.of(
            "message", "Payment successful!",
            "orderId", paidOrder.getId(),
            "status", paidOrder.getStatus()
        ));
    }

    @GetMapping("/track/{orderId}")
    @PreAuthorize("hasAuthority('ROLE_USER')")
    public String trackOrder(@PathVariable Long orderId) {
        Order order = orderService.getOrderById(orderId);
        return "Order Status: " + order.getStatus();
    }

    @GetMapping("/customer/{email}")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomer(@PathVariable String email) {
        List<OrderResponse> orders = orderService.getOrdersByCustomer(email);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/latest")
    public ResponseEntity<?> getLatestOrder(HttpServletRequest request) {
        String email = jwtUtil.extractEmailFromRequest(request);
        OrderResponse order = orderService.getLatestOrder(email);

        if (order == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No recent order found.");
        }

        return ResponseEntity.ok(order);
    }
}
