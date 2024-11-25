package com.sqe.finals.controller;

import com.sqe.finals.entity.*;
import com.sqe.finals.exception.ResourceNotFoundException;
import com.sqe.finals.repository.CategoryRepository;
import com.sqe.finals.repository.ProductSizeRepository;
import com.sqe.finals.service.CategoryService;
import com.sqe.finals.service.ProductService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    private final String supabaseBaseUrl = "https://ozptbbwzmxdbmzgeyqmf.supabase.co/storage/v1/object/public/"; // Supabase public URL

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
        // Check if the category is provided
        if (product.getCategory() == null || product.getCategory().getId() == null) {
            throw new IllegalArgumentException("Category ID must be provided.");
        }

        // Fetch the category from the repository using the category ID
        Long categoryId = product.getCategory().getId(); // Get the category ID from the product

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found for this id :: " + categoryId));

        // Set the fetched category to the product
        product.setCategory(category);

        // Save the product
        Product savedProduct = productService.createProduct(product);

        // Return the saved product
        return ResponseEntity.ok(savedProduct);
    }




    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductDTO> productDTOs = new ArrayList<>();

        // Map each product to a DTO and append full image URLs
        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setSizes(product.getSizes());

            // Construct full image URLs
            productDTO.setImageSideUrl(supabaseBaseUrl + product.getImageSide());
            productDTO.setImageTopUrl(supabaseBaseUrl + product.getImageTop());

            // Map category information if available
            if (product.getCategory() != null) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                productDTO.setCategory(categoryDTO); // Set category info in DTO
            }

            productDTOs.add(productDTO);
        }

        return ResponseEntity.ok(productDTOs);
    }


    // Endpoint to fetch all the products will use this for individual product page
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setDescription(product.getDescription());
        productDTO.setPrice(product.getPrice());
        productDTO.setSizes(product.getSizes());

        // Construct full image URLs
        productDTO.setImageSideUrl(supabaseBaseUrl + product.getImageSide());
        productDTO.setImageTopUrl(supabaseBaseUrl + product.getImageTop());

        // Map category information if available
        if (product.getCategory() != null) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(product.getCategory().getId());
            categoryDTO.setName(product.getCategory().getName());
            productDTO.setCategory(categoryDTO); // Set category info in DTO
        }

        return ResponseEntity.ok(productDTO);
    }

    // Endpoint to update an existing product ADMIN-ONLY endpoint
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product productDetails) {
        Product updatedProduct = productService.updateProduct(id, productDetails);
        return ResponseEntity.ok(updatedProduct);
    }

    // Endpoint to delete a product ADMIN-ONLY endpoint
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable Long categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        List<ProductDTO> productDTOs = new ArrayList<>();

        for (Product product : products) {
            ProductDTO productDTO = new ProductDTO();
            productDTO.setId(product.getId());
            productDTO.setName(product.getName());
            productDTO.setDescription(product.getDescription());
            productDTO.setPrice(product.getPrice());
            productDTO.setSizes(product.getSizes());
            productDTO.setImageSideUrl(supabaseBaseUrl + product.getImageSide());
            productDTO.setImageTopUrl(supabaseBaseUrl + product.getImageTop());

            if (product.getCategory() != null) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(product.getCategory().getId());
                categoryDTO.setName(product.getCategory().getName());
                productDTO.setCategory(categoryDTO);
            }

            productDTOs.add(productDTO);
        }

        return ResponseEntity.ok(productDTOs);
    }

}


