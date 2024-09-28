package com.sqe.finals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/error")//Just used this as a template page so I don't have to see whitelabel errors
    public String error() {
        return "error"; // Return the name of the login page (Thymeleaf, JSP, etc.)
    }
}
