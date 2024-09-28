package com.sqe.finals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login") //View template again using thymeleaf so I can skip using postman for now
    public String login() {
        return "login"; // Return the name of the login page (Thymeleaf, JSP, etc.)
    }
}