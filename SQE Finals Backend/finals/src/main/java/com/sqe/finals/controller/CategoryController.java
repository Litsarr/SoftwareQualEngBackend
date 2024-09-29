package com.sqe.finals.controller;

import com.sqe.finals.entity.Category;
import com.sqe.finals.service.CategoryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//Crud functions for categories (Not really necessary tbh just good to have)
@RestController
@RequestMapping("/api/categories") // Kept under the admin path
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAllCategories();
    }
}
