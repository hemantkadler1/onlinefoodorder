package com.foodapp.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String path = request.getRequestURI();
        System.out.println("[JwtFilter] Request URI: " + path);

        if (path.contains("/api/users/register") || path.contains("/api/users/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")
                && SecurityContextHolder.getContext().getAuthentication() == null) {

            String token = authHeader.substring(7);

            try {
                if (jwtUtil.validateToken(token)) {
                    Claims claims = jwtUtil.extractAllClaims(token);
                    String email = claims.getSubject();

                    @SuppressWarnings("unchecked")
                    List<String> roles = claims.get("roles", List.class);

                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());

                    UserDetails userDetails = new org.springframework.security.core.userdetails.User(
                            email, "", authorities
                    );

                    UsernamePasswordAuthenticationToken authentication
                            = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    System.out.println("[JwtFilter] Invalid JWT token.");
                }
            } catch (Exception e) {
                System.out.println("[JwtFilter] Token error: " + e.getMessage());
            }
        } else {
            if (authHeader == null) {
                System.out.println("[JwtFilter] No Authorization header.");
            } else if (!authHeader.startsWith("Bearer ")) {
                System.out.println("[JwtFilter] Authorization header format is invalid.");
            } else {
                System.out.println("[JwtFilter] Authentication already exists in context.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
