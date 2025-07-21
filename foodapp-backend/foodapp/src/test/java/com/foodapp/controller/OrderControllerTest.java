package com.foodapp.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodapp.dto.OrderRequest;
import com.foodapp.model.Order;
import com.foodapp.security.JwtFilter;
import com.foodapp.security.JwtUtil;
import com.foodapp.service.OrderService;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(OrderControllerTest.TestConfig.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderService orderService;

    @Configuration
    static class TestConfig {

        @Bean
        public OrderService orderService() {
            return mock(OrderService.class);
        }

        @Bean
        public JwtFilter jwtFilter() {
            return mock(JwtFilter.class);
        }

        @Bean
        public JwtUtil jwtUtil() {
            return mock(JwtUtil.class);
        }

        @Bean
        public AuthenticationManager authenticationManager() {
            return mock(AuthenticationManager.class);
        }
    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderRequest request = new OrderRequest();
        request.setCustomerEmail("test@example.com");
        request.setItemIds(Arrays.asList(1L, 2L));
        request.setQuantities(Arrays.asList(2, 1));

        Order mockOrder = new Order();
        mockOrder.setId(101L);
        mockOrder.setStatus("PLACED");

        Mockito.when(orderService.placeOrder(Mockito.any(OrderRequest.class)))
                .thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders/place")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101L))
                .andExpect(jsonPath("$.status").value("PLACED"));
    }
}
