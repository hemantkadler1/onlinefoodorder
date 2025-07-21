package com.foodapp.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodapp.dto.OrderRequest;
import com.foodapp.dto.OrderResponse;
import com.foodapp.model.MenuItem;
import com.foodapp.model.Order;
import com.foodapp.model.OrderItem;
import com.foodapp.repository.MenuItemRepository;
import com.foodapp.repository.OrderRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class OrderService {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Order placeOrder(OrderRequest orderRequest) {
        String email = orderRequest.getCustomerEmail();
        log.info("Placing order for {}", email);

        List<Long> itemIds = orderRequest.getItemIds();
        List<Integer> quantities = orderRequest.getQuantities();

        if (itemIds.size() != quantities.size()) {
            throw new IllegalArgumentException("Item IDs and quantities must match in size.");
        }

        List<OrderItem> orderItems = new ArrayList<>();
        double total = 0;

        Order order = Order.builder()
                .customerEmail(email)
                .status("PENDING")
                .createdAt(new Date())
                .build();

        for (int i = 0; i < itemIds.size(); i++) {
            Long itemId = itemIds.get(i);
            int quantity = quantities.get(i);

            MenuItem menuItem = menuItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("Item not found: " + itemId));

            double itemTotal = menuItem.getPrice() * quantity;

            OrderItem orderItem = OrderItem.builder()
                    .menuItem(menuItem)
                    .quantity(quantity)
                    .price(menuItem.getPrice())
                    .order(order)
                    .build();

            orderItems.add(orderItem);
            total += itemTotal;
        }

        order.setItems(orderItems);
        order.setTotalPrice(total);

        return orderRepository.save(order);
    }

    public List<Order> getOrdersByEmail(String email) {
        log.info("Fetching orders for {}", email);
        return orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
    }

    public List<OrderResponse> getOrdersByCustomer(String email) {
        List<Order> orders = orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
        return orders.stream()
                .map(order -> new OrderResponse(order.getId(), order.getStatus(), order.getTotalPrice()))
                .collect(Collectors.toList());
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public Order getOrderById(Long id) {
        log.info("Fetching order with ID: {}", id);
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with ID: " + id));
    }

    public OrderResponse getLatestOrder(String email) {
        List<Order> orders = orderRepository.findByCustomerEmailOrderByCreatedAtDesc(email);
        if (orders.isEmpty()) {
            return null;
        }

        Order latest = orders.get(0);

        // Use totalPrice already calculated when placing order
        return new OrderResponse(latest.getId(), latest.getStatus(), latest.getTotalPrice());
    }

}
