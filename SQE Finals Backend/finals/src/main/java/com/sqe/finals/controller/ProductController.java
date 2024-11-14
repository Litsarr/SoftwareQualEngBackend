package com.sqe.finals.controller;

import com.sqe.finals.entity.Category;
import com.sqe.finals.entity.Product;
import com.sqe.finals.entity.ProductSize;
import com.sqe.finals.exception.ResourceNotFoundException;
import com.sqe.finals.repository.CategoryRepository;
import com.sqe.finals.repository.ProductSizeRepository;
import com.sqe.finals.service.CategoryService;
import com.sqe.finals.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository; // Assuming you have a repository for Category

    @Autowired
    private ProductSizeRepository productSizeRepository;

    // Endpoint to create a new product ADMIN-ONLY endpoint
    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Fetch the category from the repository
        Long categoryId = product.getCategory().getId(); // Get the category ID from the product

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + categoryId));

        // Set the fetched category to the product
        product.setCategory(category);

        // Save the product
        Product savedProduct = productService.createProduct(product);
        return ResponseEntity.ok(savedProduct);
    }



    // Endpoint to fetch all the products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(products);
    }

    // Endpoint to fetch all the products will use this for individual product page
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }

    // Endpoint to update a product ADMIN-ONLY endpoint
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // Endpoint to remove a product ADMIN-ONLY endpoint
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}


