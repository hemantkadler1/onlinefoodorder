package com.foodapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodapp.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerEmailOrderByCreatedAtDesc(String email);

}
