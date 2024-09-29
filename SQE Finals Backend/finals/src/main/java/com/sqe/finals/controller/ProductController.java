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
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Create a new product
    @PostMapping
    @Transactional
    public ResponseEntity<Product> addProduct(@ModelAttribute Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    // Read all products
    @GetMapping("/view")
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

    // Update a specific product by ID
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Product updatedProduct = productService.updateProduct(id, product);
        if (updatedProduct == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedProduct);
    }

    // Delete a specific product by ID
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

