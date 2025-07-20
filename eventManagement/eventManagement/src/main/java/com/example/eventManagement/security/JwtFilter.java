package com.example.eventManagement.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String uri = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");

        // ✅ Allow public endpoints and static files
        if (uri.startsWith("/api/auth") || uri.endsWith(".html") || uri.endsWith(".js") || uri.endsWith(".css")) {
            filterChain.doFilter(request, response);
            return;
        }

        // ✅ Protected endpoints require token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            // Validate token (optional: add real logic here)
            if (!isValidToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Invalid or expired token");
                return;
            }
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Authorization header missing");
            return;
        }

        filterChain.doFilter(request, response);
    }

    // Dummy validation method for now
    private boolean isValidToken(String token) {
        // In real case: decode token, check expiry, secret key match, etc.
        return token != null && !token.trim().isEmpty();
    }
}
