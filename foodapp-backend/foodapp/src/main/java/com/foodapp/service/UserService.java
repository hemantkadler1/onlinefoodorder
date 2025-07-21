package com.foodapp.service;

import com.foodapp.dto.LoginRequest;
import com.foodapp.model.User;
import com.foodapp.repository.UserRepository;
import com.foodapp.security.CustomUserDetailsService;
import com.foodapp.security.JwtUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public User register(User user) {
        log.info("Attempting to register user: {}", user.getEmail());

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            log.warn("Registration failed: Email already exists - {}", user.getEmail());
            throw new RuntimeException("Email is already registered");
        }

        user.setPassword(encoder.encode(user.getPassword()));

        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("ROLE_USER");
        }

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getEmail());
        return savedUser;
    }

    public String login(LoginRequest loginRequest) {
        log.info("Login attempt for: {}", loginRequest.getEmail());

        Optional<User> userOpt = userRepository.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (encoder.matches(loginRequest.getPassword(), user.getPassword())) {
                log.info("Login successful for: {}", user.getEmail());

                UserDetails userDetails = customUserDetailsService.loadUserByUsername(user.getEmail());
                return jwtUtil.generateToken(userDetails);
            }
        }

        log.error("Invalid login credentials for: {}", loginRequest.getEmail());
        throw new RuntimeException("Invalid email or password");
    }
}
