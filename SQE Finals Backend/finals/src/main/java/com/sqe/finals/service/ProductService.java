package com.sqe.finals.service;

import com.sqe.finals.entity.Category;
import com.sqe.finals.entity.Product;
import com.sqe.finals.repository.CategoryRepository;
import com.sqe.finals.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // Create a new product
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    // Read all products
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Read a specific product by ID
    public Product getProductById(Long id) {
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null); // Return null if not found
    }

    // Update a specific product by ID
    public Product updateProduct(Long id, Product product) {
        if (!productRepository.existsById(id)) {
            return null; // Not found
        }
        product.setId(id); // Set the ID of the product to update
        return productRepository.save(product);
    }

    // Delete a specific product by ID
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }
}

