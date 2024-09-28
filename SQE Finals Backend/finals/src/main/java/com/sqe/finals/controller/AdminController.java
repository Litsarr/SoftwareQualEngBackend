package com.sqe.finals.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @GetMapping("/admin/dashboard")
    public String dashboard() {
        return "admin/dashboard"; //Returns the view in the templates folder in resources para di na ko mag popostman
    }
}
