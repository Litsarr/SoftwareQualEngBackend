package com.sqe.finals.controller;

import com.sqe.finals.entity.Product;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.sqe.finals.service.ProductService;

import java.util.List;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("Current CSRF Token: " + csrfToken.getToken());
        model.addAttribute("_csrf", csrfToken); // Make it available in the model
        return "admin/dashboard"; // Your Thymeleaf view name
    }
}
