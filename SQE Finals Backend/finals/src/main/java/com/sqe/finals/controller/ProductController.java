package com.sqe.finals.controller;

import com.sqe.finals.entity.Product;
import com.sqe.finals.repository.ProductRepository;
import com.sqe.finals.service.ProductService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Read all products
    @GetMapping()
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        System.out.println("Products size: " + products.size());
        model.addAttribute("products", products);
        return "Number of existing products: " + products.size();
    }

    // Read a specific product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(product);
    }
}

