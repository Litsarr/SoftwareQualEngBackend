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
@RequestMapping("/admin/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public List<Category> getAllCategories() {
        return categoryService.findAllCategories();
    }

    @PostMapping
    @Transactional
    public Category createCategory(@RequestParam String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("Add Category Session ID: " + session.getId());

        // Log the CSRF token in the request
        String csrfToken = request.getHeader("X-CSRF-TOKEN");
        System.out.println("Received CSRF Token: " + csrfToken);
        return categoryService.createCategory(name);
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}

