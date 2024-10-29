package com.sqe.finals.controller;

import com.sqe.finals.entity.Admin;
import com.sqe.finals.service.AdminService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;


    // Register a new admin (only accessible by authenticated admins)
    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')") // Ensures that only authenticated admins can access this method
    public ResponseEntity<String> registerAdmin(@RequestBody @Valid Admin admin) {
        // Encrypt the password and set role
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setRole("ADMIN"); // Ensure role is set to ADMIN
        adminService.save(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }

    // Login for an existing admin
    @PostMapping("/login")
    public ResponseEntity<String> loginAdmin(@RequestBody Admin loginRequest, HttpServletRequest request, HttpServletResponse response) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Create a new session
            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

            // Retrieve the CSRF token
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

            // Set the CSRF token cookie
            if (csrfToken != null) {
                Cookie cookie = new Cookie("XSRF-TOKEN", csrfToken.getToken());
                cookie.setPath("/"); // Set the path for the cookie
                cookie.setHttpOnly(false); // Make it accessible from JavaScript if needed
                response.addCookie(cookie); // Add the cookie to the response
            }

            return ResponseEntity.ok("Login successful");
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }






}
