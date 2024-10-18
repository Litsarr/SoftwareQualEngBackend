package com.sqe.finals.controller;

import com.sqe.finals.entity.Category;
import com.sqe.finals.entity.Product;
import com.sqe.finals.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/view-all")
    public List<Product> getAllProducts() {
        return productService.findAllProducts();
    }

    @GetMapping("/view-prod/{id}")  // Endpoint to fetch a single product by ID
    public Product getProductById(@PathVariable Long id) {
        return productService.findProductById(id);
    }

    @PostMapping("/add-prod")
    public Product createProduct(@RequestBody Product product, @RequestParam Long categoryId) {
        return productService.saveProduct(product, categoryId);
    }

    @PutMapping("/update/{id}")
    public Product updateProduct(@PathVariable Long id, @RequestBody Product product) {
        // Fetch the existing product
        Product existingProduct = productService.findProductById(id);

        // Update the fields of the existing product
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setImageSide(product.getImageSide());
        existingProduct.setImageTop(product.getImageTop());

        // Handle category update if provided
        if (product.getCategory() != null && product.getCategory().getId() != null) {
            Long categoryId = product.getCategory().getId();
            Category category = productService.getCategoryById(categoryId); // Adjusted to use a service method
            existingProduct.setCategory(category);
        }

        // Save the updated product
        return productService.saveProduct(existingProduct, existingProduct.getCategory() != null ? existingProduct.getCategory().getId() : null);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}

