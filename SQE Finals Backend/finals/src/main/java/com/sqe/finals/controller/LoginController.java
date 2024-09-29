package com.sqe.finals.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login") //View template again using thymeleaf so I can skip using postman for now
    public String login(HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("Session ID: " + session.getId());

        return "login"; // Return the name of the login page (Thymeleaf, JSP, etc.)
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response,

                         Authentication authentication) {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);

        }
        return "redirect:/login";

    }
}