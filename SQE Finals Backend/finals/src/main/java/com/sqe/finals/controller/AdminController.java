package com.sqe.finals.controller;

import com.sqe.finals.entity.Admin;
import com.sqe.finals.security.JwtUtil;
import com.sqe.finals.service.AdminService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Login for an existing admin
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginAdmin(@RequestBody Admin loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Generate a JWT token after successful authentication
            String jwtToken = jwtUtil.generateToken(loginRequest.getUsername());

            // Return a JSON response with the token
            Map<String, String> responseMap = new HashMap<>();
            responseMap.put("message", "Login successful");
            responseMap.put("token", jwtToken);  // Include the JWT token in the response

            return ResponseEntity.ok(responseMap); // Return the response as JSON
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password"));
        }
    }
    // Logout endpoint
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.setAttribute(CsrfToken.class.getName(), null);
            // Invalidate the session
            session.invalidate();
        }
        return ResponseEntity.ok("Logged out successfully");
    }
}


