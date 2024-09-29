package com.sqe.finals.controller;

import com.sqe.finals.entity.Category;
import com.sqe.finals.entity.OrderStatus;
import com.sqe.finals.entity.Orders;
import com.sqe.finals.entity.Product;
import com.sqe.finals.service.CategoryService;
import com.sqe.finals.service.OrderService;
import com.sqe.finals.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/admin") // Group all admin routes
public class AdminController {

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpServletRequest request) {
        CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
        System.out.println("Current CSRF Token: " + csrfToken.getToken());
        model.addAttribute("_csrf", csrfToken); // Make it available in the model
        return "admin/dashboard"; // Your Thymeleaf view name
    }

    // Create a new product
    @PostMapping("/products")
    @Transactional
    public ResponseEntity<Product> addProduct(@ModelAttribute Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Update a specific product by ID
    @PutMapping("/products/{id}")
    @Transactional
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a specific product by ID
    @DeleteMapping("/products/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Admin-only endpoint to get all orders
    @GetMapping("/orders")
    public List<Orders> getAllOrders() {
        return orderService.findAllOrders();
    }

    // Admin-only endpoint to update order status
    @PatchMapping("/orders/{id}/status")
    public void updateOrderStatus(@PathVariable Long id, @RequestParam OrderStatus status) {
        orderService.updateOrderStatus(id, status);
    }

    // Admin-only endpoint to create a new category
    @PostMapping("/categories")
    @Transactional
    public Category createCategory(@RequestParam String name, HttpServletRequest request) {
        HttpSession session = request.getSession();
        System.out.println("Add Category Session ID: " + session.getId());

        // Log the CSRF token in the request
        String csrfToken = request.getHeader("X-CSRF-TOKEN");
        System.out.println("Received CSRF Token: " + csrfToken);
        return categoryService.createCategory(name);
    }

    // Admin-only endpoint to delete a category by ID
    @DeleteMapping("/categories/{id}")
    @Transactional
    public void deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
    }
}
