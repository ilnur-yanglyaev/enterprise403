// com.example.lab2.filter.JwtAuthFilter
package com.example.lab2.filter;

import com.example.lab2.Service.UserDetailsServiceImpl;
import com.example.lab2.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

//@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userService;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // "Bearer " → 7 символов

            try {
                // Извлекаем username из токена
                String username = jwtUtil.getUsernameFromToken(token);

                // Проверяем, что пользователь не аутентифицирован
                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                    // Загружаем пользователя (и его роли!) через UserService
                    UserDetails userDetails = userService.loadUserByUsername(username);

                    // Проверяем валидность токена
                    if (jwtUtil.validateToken(token, userDetails)) {
                        // Создаём Authentication объект
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails,
                                        null,
                                        userDetails.getAuthorities()
                                );
                        // Кладём в контекст безопасности
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
            } catch (Exception e) {
                // Логируем ошибку (опционально)
                System.out.println("JWT validation failed: " + e.getMessage());
            }
        }

        // Продолжаем цепочку фильтров
        filterChain.doFilter(request, response);
    }
}