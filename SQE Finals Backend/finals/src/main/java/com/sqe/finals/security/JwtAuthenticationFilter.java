package com.sqe.finals.security;

import com.sqe.finals.service.CustomAdminDetailsService;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomAdminDetailsService customAdminDetailsService;

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtUtil jwtUtil, CustomAdminDetailsService customAdminDetailsService) {
        this.jwtUtil = jwtUtil;
        this.customAdminDetailsService = customAdminDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException, java.io.IOException {

        logger.debug("Processing request to {}", request.getRequestURI());

        // Skip JWT validation for public endpoints
        if (isPublicEndpoint(request)) {
            logger.debug("Skipping JWT validation for public endpoint: {}", request.getRequestURI());
            filterChain.doFilter(request, response);  // Proceed without any JWT validation
            return;
        }

        // Proceed with JWT validation for protected routes
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String username;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No JWT token found in request header for {}", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        username = jwtUtil.extractUsername(jwtToken);

        logger.debug("JWT token found: {}", jwtToken);
        logger.debug("Extracted username from JWT token: {}", username);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = customAdminDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(jwtToken, userDetails.getUsername())) {
                logger.debug("JWT token is valid for username: {}", username);
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else {
                logger.debug("JWT token is not valid for username: {}", username);
            }
        }

        filterChain.doFilter(request, response);
    }

    // Check if the request matches any public endpoints
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getRequestURI();

        // Public endpoints
        if (path.startsWith("/cart") ||
                path.startsWith("/products") ||
                path.startsWith("/orders/checkout")) {

            // Paths that are public
            if (path.equals("/cart/addItem") || path.equals("/orders/checkout")) {
                return true;  // These endpoints are public
            }

            // Check if the path is specifically for product creation or update (admin-only)
            if (path.equals("/products/create") || path.equals("/products/update/{id}") || path.equals("/products/delete/{id}")) {
                return false;  // These endpoints are admin-only, so don't mark them as public
            }

            // Allow public access for other /products routes (like fetching products)
            return true;
        }

        // Admin routes (e.g., /admin/login) are public
        if (path.startsWith("/admin/login")) {
            return true;  // /admin/login is public
        }

        return false;  // All other paths require JWT authentication
    }
}