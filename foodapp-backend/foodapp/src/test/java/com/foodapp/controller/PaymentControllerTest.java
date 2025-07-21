package com.foodapp.controller;

import com.foodapp.security.JwtFilter;
import com.foodapp.security.JwtUtil;
import com.foodapp.service.PaymentService;

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

import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(PaymentControllerTest.TestConfig.class)
public class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentService paymentService;

    @Test
    public void testPaymentSuccess() throws Exception {
        Long orderId = 1L;
        String method = "card";

        Mockito.when(paymentService.processPayment(orderId, method)).thenReturn(true);

        mockMvc.perform(post("/api/payments/pay/{orderId}?method={method}", orderId, method)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment successful for order ID: " + orderId));
    }

    @Test
    public void testPaymentFailure() throws Exception {
        Long orderId = 2L;
        String method = "failMethod";

        Mockito.when(paymentService.processPayment(orderId, method)).thenReturn(false);

        mockMvc.perform(post("/api/payments/pay/{orderId}?method={method}", orderId, method)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Payment failed for order ID: " + orderId));
    }

    @Configuration
    static class TestConfig {

        @Bean
        public PaymentService paymentService() {
            return mock(PaymentService.class);
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
}
